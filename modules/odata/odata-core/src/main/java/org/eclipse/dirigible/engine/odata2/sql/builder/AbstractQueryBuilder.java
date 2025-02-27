/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: 2021 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.dirigible.engine.odata2.sql.builder;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.*;
import org.eclipse.dirigible.commons.config.Configuration;
import org.eclipse.dirigible.engine.odata2.sql.api.OData2Exception;
import org.eclipse.dirigible.engine.odata2.sql.api.SQLStatementBuilder;
import org.eclipse.dirigible.engine.odata2.sql.api.SQLStatementParam;
import org.eclipse.dirigible.engine.odata2.sql.binding.EdmTableBinding;
import org.eclipse.dirigible.engine.odata2.sql.binding.EdmTableBindingProvider;
import org.eclipse.dirigible.engine.odata2.sql.clause.SQLJoinClause;
import org.eclipse.dirigible.engine.odata2.sql.clause.SQLWhereClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.olingo.odata2.api.commons.HttpStatusCodes.INTERNAL_SERVER_ERROR;
import static org.eclipse.dirigible.engine.odata2.sql.binding.EdmTableBinding.ColumnInfo;
import static org.eclipse.dirigible.engine.odata2.sql.utils.OData2Utils.fqn;

public abstract class AbstractQueryBuilder implements SQLStatementBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(SQLSelectBuilder.class);
    private final Map<String, EdmStructuralType> tableAliasesForEntitiesInQuery;
    private final Set<String> structuralTypesInJoin;

    private final EdmTableBindingProvider tableBinding;
    private final List<SQLJoinClause> joinExpressions = new ArrayList<>();
    private final List<SQLStatementParam> sqlStatmenetParams;
    private SQLWhereClause whereClause;

    public AbstractQueryBuilder(final EdmTableBindingProvider tableBinding) {
        this.tableBinding = tableBinding;
        this.whereClause = new SQLWhereClause();
        this.tableAliasesForEntitiesInQuery = new TreeMap<>();
        this.structuralTypesInJoin = new HashSet<>();
        this.sqlStatmenetParams = new ArrayList<>();
    }


    public List<SQLStatementParam> getStatementParams() {
        return sqlStatmenetParams;
    }

    public void addStatementParam(SQLStatementParam param) {
        sqlStatmenetParams.add(param);
    }

    public SQLWhereClause getWhereClause() {
        return whereClause;
    }

    public void addStatementParam(EdmNavigationProperty entity, EdmProperty property, Object value) throws EdmException {
        EdmType edmType = entity.getType();
        if (edmType instanceof EdmEntityType) {
            addStatementParam((EdmEntityType) edmType, property, value);
        } else {
            throw new OData2Exception("Not implemented", HttpStatusCodes.NOT_IMPLEMENTED);
        }
    }

    public void addStatementParam(EdmStructuralType entity, EdmProperty property, Object value) throws EdmException {
        if (property.isSimple()) {
            EdmTableBinding.ColumnInfo info = getSQLTableColumnInfo(entity, property);
            addStatementParam(new SQLStatementParam(value, (EdmSimpleType) property.getType(), info));
        } else {
            throw new IllegalArgumentException("Not Implemented");
        }
    }

    public EdmTableBindingProvider getTableBinding() {
        return tableBinding;
    }

    public String getSQLTableName(final EdmStructuralType target) { //TODO use context
        boolean caseSensitive = Boolean.parseBoolean(
                Configuration.get("DIRIGIBLE_DATABASE_NAMES_CASE_SENSITIVE", "false"));
        EdmTableBinding mapping = tableBinding.getEdmTableBinding(target);
        String tableName = mapping.getTableName();
        return caseSensitive ? ("\"" + tableName + "\"") : tableName;
    }

    public List<String> getSQLJoinTableName(final EdmStructuralType from, final EdmStructuralType to) throws EdmException {
        if (tableBinding.getEdmTableBinding(from).hasJoinColumnTo(to))
            return tableBinding.getEdmTableBinding(from).getJoinColumnTo(to);
        throw new IllegalArgumentException("No join column definition found from type " + from + " to type " + to);
    }

    public String getSQLTablePrimaryKey(final EdmStructuralType type) throws EdmException {
        return tableBinding.getEdmTableBinding(type).getPrimaryKey();
    }

    public String getSQLTableColumn(final EdmStructuralType targetEnitityType, final EdmProperty p) {
        if (p.isSimple()) {
            boolean caseSensitive = Boolean.parseBoolean(Configuration.get("DIRIGIBLE_DATABASE_NAMES_CASE_SENSITIVE", "false"));
            if (caseSensitive) {
                return "\"" + getSQLTableAlias(targetEnitityType) + "\".\"" + tableBinding.getEdmTableBinding(targetEnitityType).getColumnName(p) + "\"";
            } else {
                return getSQLTableAlias(targetEnitityType) + "." + tableBinding.getEdmTableBinding(targetEnitityType).getColumnName(p);
            }
        } else {
            throw new IllegalArgumentException("Unable to get the table column name of complex property " + p);
        }
    }

    public String getSQLTableColumnNoAlias(final EdmStructuralType targetEnitityType, final EdmProperty p) {
        if (p.isSimple()) {
            boolean caseSensitive = Boolean.parseBoolean(Configuration.get("DIRIGIBLE_DATABASE_NAMES_CASE_SENSITIVE", "false"));
            if (caseSensitive) {
                return "\"" + tableBinding.getEdmTableBinding(targetEnitityType).getColumnName(p) + "\"";
            } else {
                return tableBinding.getEdmTableBinding(targetEnitityType).getColumnName(p);
            }
        } else {
            throw new IllegalArgumentException("Unable to get the table column name of complex property " + p);
        }
    }

    //TODO use me
    private String fixDatabaseNamesCase(String column) {
        boolean caseSensitive = Boolean.parseBoolean(Configuration.get("DIRIGIBLE_DATABASE_NAMES_CASE_SENSITIVE", "false"));
        return caseSensitive ? "\"" + column + "\"" : column;
    }


    public List<String> getSQLJoinColumnNoAlias(final EdmStructuralType targetEnitityType, final EdmNavigationProperty p) throws EdmException {
        List<String> joinColums = tableBinding.getEdmTableBinding(targetEnitityType).getJoinColumnTo((EdmStructuralType) p.getType());
        return joinColums.stream().map(this::fixDatabaseNamesCase).collect(Collectors.toList());
    }

    public ColumnInfo getSQLTableColumnInfo(final EdmStructuralType targetEnitityType, final EdmProperty p) throws EdmException {
        if (p.isSimple()) {
            ColumnInfo info = tableBinding.getEdmTableBinding((targetEnitityType)).getColumnInfo(p);
            boolean caseSensitive = Boolean.parseBoolean(Configuration.get("DIRIGIBLE_DATABASE_NAMES_CASE_SENSITIVE", "false"));
            if (caseSensitive) {
                return new ColumnInfo("\"" + getSQLTableAlias(targetEnitityType) + "\".\"" + info.getColumnName() + "\"", info.getJdbcType());
                //return getSQLTableAlias(targetEnitityType) + "." + tableMappingProvider.getTableMapping(targetEnitityType).getColumnName(p);
            } else {
                return new ColumnInfo(getSQLTableAlias(targetEnitityType) + "." + info.getColumnName(), info.getJdbcType());
                //return getSQLTableAlias(targetEnitityType) + "." + tableMappingProvider.getTableMapping(targetEnitityType).getColumnName(p);
            }
        } else {
            throw new IllegalArgumentException("Unable to get the table column name of complex property " + p);
        }
    }

    public String getSQLTableColumnAlias(final EdmStructuralType targetEnitityType, final EdmProperty property) {
        if (property.isSimple())
            return tableBinding.getEdmTableBinding(targetEnitityType).getColumnName(property) + "_" + getSQLTableAlias(targetEnitityType);
        else
            throw new IllegalArgumentException("Unable to get the table column name of complex property " + property);
    }

    public boolean isTransientType(final EdmStructuralType targetEnitityType, final EdmProperty property) {
        return !tableBinding.getEdmTableBinding(targetEnitityType).isPropertyMapped(property);
    }


    // This Method is for internal use ONLY !!! Do NEVER use it !!!
    // It will be hidden in future without further mitigation
    // TODO Refactor this method to private area
    public String getSQLTableAlias(final EdmType type) {
        if (type instanceof EdmStructuralType)
            return getTableAliasForType((EdmStructuralType) type);
        else
            throw new IllegalArgumentException("Mapping of types other than EdmEntityType and EdmComplexType is not supported!");
    }

    private String getTableAliasForType(final EdmStructuralType st) {
        Collection<String> keys = tableAliasesForEntitiesInQuery.keySet();
        try {
            for (String key : keys) {
                EdmStructuralType type = tableAliasesForEntitiesInQuery.get(key);
                if (fqn(type).equals(fqn(st)))
                    return key;
            }
            return grantTableAliasForStructuralTypeInQuery(st);
        } catch (Exception e) {
            throw new OData2Exception("No mapping has been defined for type " + fqn(st), INTERNAL_SERVER_ERROR);
        }
    }

    // This Method is for internal use ONLY !!! Do NEVER use it !!!
    // It will be hidden in future without further mitigation
    // TODO Refactor this method to private area

    /**
     * Get table aliases
     *
     * @return the aliases
     */
    public Iterator<String> getTablesAliasesForEntitiesInQuery() {
        return tableAliasesForEntitiesInQuery.keySet().iterator();
    }

    // This Method is for internal use ONLY !!! Do NEVER use it !!!
    // It will be hidden in future without further mitigation
    // TODO Refactor this method to private area
    public EdmStructuralType getEntityInQueryForAlias(final String tableAlias) {
        return tableAliasesForEntitiesInQuery.get(tableAlias);
    }

    // This Method is for internal use ONLY !!! Do NEVER use it !!!
    // It will be hidden in future without further mitigation
    // TODO Refactor this method to private area
    public String grantTableAliasForStructuralTypeInQuery(final EdmStructuralType entity) {
        try {
            Collection<EdmStructuralType> targets = tableAliasesForEntitiesInQuery.values();
            for (EdmStructuralType type : targets) {
                if (fqn(type).equals(fqn(entity)))
                    // Alias is already contained in the map
                    return getTableAliasForType(type);
            }
            String alias = "T" + tableAliasesForEntitiesInQuery.size();
            LOG.debug("Grant Alias '" + alias + "' for " + entity.getName());
            // Add alias to map
            tableAliasesForEntitiesInQuery.put("T" + tableAliasesForEntitiesInQuery.size(), entity);
            return alias;
        } catch (EdmException e) {
            throw new OData2Exception(INTERNAL_SERVER_ERROR, e);
        }
    }
}
