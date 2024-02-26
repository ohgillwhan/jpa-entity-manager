package persistence.sql.dialect;

import jakarta.persistence.GenerationType;

import java.util.HashMap;
import java.util.Map;

public abstract class Dialect {
    private final Map<Integer, String> columnTypeMap = new HashMap<Integer, String>();

    protected void registerColumnType(int code, String name) {
        columnTypeMap.put(code, name);
    }

    public String getColumnType(final int typeCode) {
        final Integer integer = Integer.valueOf(typeCode);
        final String result = columnTypeMap.get(integer);
        if (result == null) {
            throw new IllegalArgumentException("No Dialect mapping for type: " + typeCode);
        }

        return result;
    }

    public abstract String generatorCreatePrimaryDdl(final GenerationType strategy);
    public abstract String getNotNullConstraint(boolean nullable);

    public abstract String getCreateDefaultDdlQuery();
    public abstract String getDropDefaultDdlQuery();
    public abstract String getInsertDefaultDmlQuery();
    public abstract String getFindAllDefaultDmlQuery();
    public abstract String getFindByIdDefaultDmlQuery();
    public abstract String getDeleteDefaultDmlQuery();
    public abstract String getUpdateDefaultDmlQuery();

}