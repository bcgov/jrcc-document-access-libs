# Sample application

## SFTP Config

run

```bash
docker-compose up
```

run

```bash
ssh-keyscan -H localhost -p 22 >> ~/.ssh/known_hosts
```

configure the following environment variables on your environment:

| Name | Value |
| --- | --- |
| KNOWN_HOSTS | ~/.ssh/known_hosts |
| spring_active_profiles | sftp |
