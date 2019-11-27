### 此源码做了小改

主要针对converters/LoadJet.java里面的部分逻辑，主要是为了修复超大整数溢出问题。

1. loadData方法：
```java
    execInsert(ps, values);
```
改成：
```java
try {
    execInsert(ps, values);
} catch (Exception e) {
    throw new SQLDataException("execInsert row=" + i + " throw exception", e);
}
```

2. execInsert方法：

```java

    private void execInsert(PreparedStatement st, List<Object> values) throws SQLException {
        int i = 1;
        for (Object value : values) {
            st.setObject(i++, value);
        }
        // st.execute();
        st.addBatch();
    }

```
改成：
```java
    private void execInsert(PreparedStatement st, List<Object> values, int rowIndex) throws SQLException {
        int i = 1;
        for (Object value : values) {
            try {
                st.setObject(i++, value);
            } catch (SQLDataException e) {
                Logger.logWarning("Load row=" + rowIndex + " column=" + i + " value=" + value + " is error:" + e.getMessage());
            }
        }
        // st.execute();
        st.addBatch();
    }

```


