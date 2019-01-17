开发人员编写的类 Application
ConfigurableApplicationContext application = springApplication.run(args);
-->
org.springframework.boot.SpringApplication
context = createApplicationContext();
-->
contextClass = Class.forName(this.webEnvironment ? DEFAULT_WEB_CONTEXT_CLASS : DEFAULT_CONTEXT_CLASS);//org.springframework.context.annotation.AnnotationConfigApplicationContext
(ConfigurableApplicationContext) BeanUtils.instantiate(contextClass);
-->
org.springframework.beans.BeanUtils
return clazz.newInstance();

-->
org.springframework.context.annotation.AnnotationConfigApplicationContext
public AnnotationConfigApplicationContext() {
    //关键两个类
    //Convenient adapter for programmatic registration of annotated bean classes
    this.reader = new AnnotatedBeanDefinitionReader(this);
    //A bean definition scanner that detects bean candidates on the classpath,registering corresponding bean definitions with a given registry (BeanFactory or ApplicationContext)
    this.scanner = new ClassPathBeanDefinitionScanner(this);
}
-->
org.springframework.context.annotation.AnnotatedBeanDefinitionReader
public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
    this(registry, getOrCreateEnvironment(registry));
}

public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry, Environment environment) {
    Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
    Assert.notNull(environment, "Environment must not be null");
    this.registry = registry;
    this.conditionEvaluator = new ConditionEvaluator(registry, environment, null);
    AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
}

-->
org.springframework.context.annotation.AnnotationConfigUtils
public static void registerAnnotationConfigProcessors(BeanDefinitionRegistry registry) {
    registerAnnotationConfigProcessors(registry, null);
}

public static Set<BeanDefinitionHolder> registerAnnotationConfigProcessors(
			BeanDefinitionRegistry registry, Object source) {
    DefaultListableBeanFactory beanFactory = unwrapDefaultListableBeanFactory(registry);
}

------------------------------------------------------------------------------------------------
https://segmentfault.com/a/1190000012887776#articleHeader5
------------------------------------------------------------------------------------------------
spring boot 初始化是怎么扫描类的

SpringApplication.run
refreshContext();
refresh();
invokeBeanFactoryPostProcessors(); --  代码行：invokeBeanDefinitionRegistryPostProcessors(priorityOrderedPostProcessors, registry);

------------------------------------------------------------------------------------------------
org.springframework.beans.factory.config.BeanDefinitionHolder
----------
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory
createBean
----------------------------------------------------
--创建Bean  https://segmentfault.com/a/1190000012887776#articleHeader5
AbstractApplicationContext

// Instantiate all remaining (non-lazy-init) singletons.
finishBeanFactoryInitialization(beanFactory);

// Instantiate all remaining (non-lazy-init) singletons.
beanFactory.preInstantiateSingletons();
----------------------------------------------------
-- BeanDefinition读取、注册  https://blog.csdn.net/u011179993/article/details/51598567
BeanDefinitionHolder -- 持有一个BeanDefinition，名称，和别名数组。在Spring内部，它用来临时保存BeanDefinition来传递BeanDefinition。

注意：类 DefaultListableBeanFactory 实现 接口 BeanDefinitionRegistry   大部分用到类型的强转将DefaultListableBeanFactory对象强转为 BeanDefinitionRegistry对象；

AbstractApplicationContext
// Invoke factory processors registered as beans in the context.
invokeBeanFactoryPostProcessors(beanFactory);

PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());

PostProcessorRegistrationDelegate
//Invoke the given BeanDefinitionRegistryPostProcessor beans.
invokeBeanDefinitionRegistryPostProcessors(priorityOrderedPostProcessors, registry);

postProcessor.postProcessBeanDefinitionRegistry(registry);

--ConfigurationClassPostProcessor.java
processConfigBeanDefinitions(registry);
parser.parse(candidates);// 重要。。。  ConfigurationClassParser.java

sourceClass = doProcessConfigurationClass(configClass, sourceClass);

// Process any @ComponentScan annotations
Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(sourceClass.getMetadata(), ComponentScans.class, ComponentScan.class);

// The config class is annotated with @ComponentScan -> perform the scan immediately
Set<BeanDefinitionHolder> scannedBeanDefinitions = this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());//重要
--找到了示例
@SpringBootApplication()
public class Application {}  -- 默认只扫描 当前启动类Application同一个包路径及子包路径下的所有类

ComponentScanAnnotationParser.java
public Set<BeanDefinitionHolder> parse(AnnotationAttributes componentScan, final String declaringClass) {
    ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this.registry,componentScan.getBoolean("useDefaultFilters"), this.environment, this.resourceLoader);
    if (basePackages.isEmpty()) {
       basePackages.add(ClassUtils.getPackageName(declaringClass));//-- 默认只扫描 当前启动类Application同一个包路径及子包路径下的所有类
    }
}

this.reader.loadBeanDefinitions(configClasses);// 重要。。。
//BeanDefinitionRegistry接口一次只能注册一个BeanDefinition，而且只能自己构造BeanDefinition类来注册。BeanDefinitionReader解决了这些问题，
//它一般可以使用一个BeanDefinitionRegistry构造，然后通过#loadBeanDefinitions（..）等方法，
//把“配置源”转化为多个BeanDefinition并注册到BeanDefinitionRegistry中.
//可以说BeanDefinitionReader帮助BeanDefinitionRegistry实现了高效、方便的注册BeanDefinition。

ConfigurationClassBeanDefinitionReader.java
loadBeanDefinitions(Set<ConfigurationClass> configurationModel)

