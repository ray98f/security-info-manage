package com.security.info.manage.config;

import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/21 11:26
 */
@Slf4j
@Configuration
public class MySqlParserFilter implements ISqlParserFilter {

    @Override
    public boolean doFilter(MetaObject metaObject) {
//        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
//        if (SqlCommandType.SELECT == mappedStatement.getSqlCommandType()) {
//            BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
//            String sql = boundSql.getSql();
//            try {
//                Select select = (Select) CCJSqlParserUtil.parse(sql);
//                SelectBody selectBody = select.getSelectBody();
//                PlainSelect plainSelect = (PlainSelect) selectBody;
//                if (!(plainSelect.getFromItem() instanceof Table)) {
//                    return false;
//                }
//            } catch (JSQLParserException e) {
//                e.printStackTrace();
//                log.error("error when parse sql ! ");
//            }
//        }
        return true;
    }
}
