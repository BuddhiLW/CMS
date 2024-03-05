# CMS - Content Management System, Microservices Pattern

The goal is to build a one db per service patterned, microservices-composed, `CMS`. So, one can change it's pieces and test different DBs, performances, UI feeling, etc. also, one can implement each front-backend duo with different languages too.

## DOING (30%): API Gateway 
### Kong:
#### Setup
``` sh
git clone https://github.com/Kong/docker-kong.git
cd docker-kong/compose
vim docker-compose.yml
```

Set external (expose net) to true,

``` yaml
networks:
  kong-net:
    external: true
```

Add Konga setup,

``` yaml
# (under services:)
  konga:
    container_name: konga
    image: pantsel/konga:latest
    restart: always
    networks:
      - kong-net
    environment:
      DB_ADAPTER: postgres
      DB_HOST: db
      DB_USER: kong
      DB_PASSWORD: kong
      TOKEN_SECRET: jPXfiYwVE9UcLTX4cyHD1ghZl
      DB_DATABASE: konga_db
      NODE_ENV: production
    depends_on:
      - db
    ports:
      - "1337:1337"

  konga-prepare:
    container_name: konga-prepare
    image: pantsel/konga:latest
    command: "-c prepare -a postgres -u postgresql://kong:kong@db:5432/konga_db"
    networks:
      - kong-net
    restart: on-failure
    links:
      - db
    depends_on:
      - db
```

Finally, create the kong-net [^1]

``` sh
sudo docker network create kong-net
```

### Konga:

## DONE (95%): Login Page

Current looks:
[./imgs/auth0-login.png](./imgs/auth0-login.png)

Main page:
[./imgs/main.png](./imgs/main.png)


# Resources and notes
[^1]: command should be issue inside docker-kong/compose/ directory.
