module service {
    requires domain;
    requires spring.context;
    requires spring.beans;
    requires org.mapstruct;
    requires static lombok;
    requires spring.boot.autoconfigure;
    exports com.ribas.andrei.training.spring.udemy.service;

    // Internal packages: NOT exported, but opened for Spring reflection at runtime
    opens com.ribas.andrei.training.spring.udemy.service.impl to spring.core, spring.beans, spring.context;
    opens com.ribas.andrei.training.spring.udemy.service.mapper to spring.core, spring.beans, spring.context;
    opens com.ribas.andrei.training.spring.udemy.service.config.auto to spring.core, spring.beans, spring.context;
}