package com.timeular.nytta.spring.datasource

import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import javax.sql.DataSource

@Configuration
open class DataSourceConfiguration {

    @Bean
    open fun retryTemplate(): RetryTemplate {

        val backoffPolicy = ExponentialBackOffPolicy()
        backoffPolicy.initialInterval = 3000
        backoffPolicy.multiplier = 1.75

        val retryTemplate = RetryTemplate()
        retryTemplate.setRetryPolicy(SimpleRetryPolicy(5))
        retryTemplate.setBackOffPolicy(backoffPolicy)

        return retryTemplate
    }

    @Bean
    open fun dataSourceWrapper(retryTemplate: RetryTemplate): BeanPostProcessor {
        return RetryableDataSourceBeanPostProcessor(retryTemplate)
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    private class RetryableDataSourceBeanPostProcessor(val retryTemplate: RetryTemplate) : BeanPostProcessor {

        override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
            if (bean is DataSource) {
                return RetryableDataSource(bean, retryTemplate)
            }

            return bean
        }

        override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
            return bean
        }
    }
}