> This method is code for creating the batch meta table needed in the test environment.
> Please refer to the Note in the top [README.md](../../../../../../../README.md).
>
>> If you want to execute it, please change the isExecuteGeneratePostgresMetaTable of [BaseBatchSettings](./BaseBatchSettings.java) to true. (default: false) The test environment by default applies the option '[hibernate.hbm2ddl.auto]': create

```java
public void generatePostgresMetaTable()
```
