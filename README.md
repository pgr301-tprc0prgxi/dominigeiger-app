# dominigeiger-app

* [App - https://github.com/pgr301-tprc0prgxi/dominigeiger-app](https://github.com/pgr301-tprc0prgxi/dominigeiger-app)
* [Infrastructure - https://github.com/pgr301-tprc0prgxi/dominigeiger-ifra](https://github.com/pgr301-tprc0prgxi/dominigeiger-ifra)

## Sending metrics to InfluxDB

To make the application send data to an InfluxDB instance, you will have to start the app with the `local` profile.
Then it assumes InfluxDB is accesible at `http://localhost:8086` and it will use the database `dominigeiger`, if
you want to change this you can change the [`src/main/resources/application-local.yml`](src/main/resources/application-local.yml#L15-L18)
file.

## Testing the applicaition

When developing I made a node script to send requests with random data, it helped me a lot with the understanding of the metrics.
I created a [gist](https://gist.github.com/pgr301-tprc0prgxi/64baf945011e99bef9b993232282e800) with the code if it is of any interest.

## Environment Variables for travis

For the pipeline to work you will have to add the following environment variables:

```
DOCKER_USERNAME
DOCKER_PASSWORD
DOCKER_IMAGE
```

*Also add the api_key for deploying to Heroku*
