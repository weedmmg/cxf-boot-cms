
package com.cxf.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.github.pagehelper.PageHelper;

/**
 * 
 * @Date: 2017年10月23日 下午1:26:50
 * @author chenxf
 */
@Configuration
@MapperScan(basePackages = "com.cxf.mapper.xrjf", sqlSessionTemplateRef = "xrjfSqlSessionTemplate")
public class XrjfDataSourceConfig {
	@Bean(name = "xrjfDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.xrjf")
	@Primary
	public DataSource testDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "xrjfSqlSessionFactory")
	@Primary
	public SqlSessionFactory xrjfSqlSessionFactory(@Qualifier("xrjfDataSource") DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		Interceptor[] plugins = new Interceptor[] { pageHelper(), sqlPrintInterceptor() };
		bean.setPlugins(plugins);
		return bean.getObject();
	}

	@Bean(name = "xrjfTransactionManager")
	@Primary
	public DataSourceTransactionManager xrjfTransactionManager(@Qualifier("xrjfDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "xrjfSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate xrjfSqlSessionTemplate(
			@Qualifier("xrjfSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	/**
	 * 分页插件
	 * 
	 * @param dataSource
	 * @return
	 */

	public PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
		Properties p = new Properties();
		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "true");
		p.setProperty("reasonable", "true");
		p.setProperty("returnPageInfo", "check");
		p.setProperty("params", "count=countSql");
		pageHelper.setProperties(p);
		return pageHelper;
	}

	// 将要执行的sql进行日志打印(不想拦截，就把这方法注释掉)
	public SqlPrintInterceptor sqlPrintInterceptor() {
		return new SqlPrintInterceptor();
	}
}
