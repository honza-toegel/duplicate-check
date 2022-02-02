# Usage
## Checking if the string exist
The following command will check if the string "abc" already exist, the metadata can be used optionally, metadata are not checked for uniqueness
```shell
curl -X POST -H 'Content-Type: application/json' -d '{"uniqueId": "abc","metadata": "123"}' -u guest:pass http://localhost:8080/DuplicateCheck/
```
Expected output after first try, indicating that id "abc" doesn't exist yet
```json
{
  "alreadyUsed":false,
  "pastUsage":null
}
```

Expected output after second try, indicating that id "abc" exist already:
```json
{
  "alreadyUsed":true,
  "pastUsage": {
    "uniqueId":"abc",
    "metadata":"123",
    "timestamp":"2022-02-02T04:02:06.449"
  }
}
```

## Statistics
```shell
curl -u guest:pass http://localhost:8080/DuplicateCheck/statistics
```