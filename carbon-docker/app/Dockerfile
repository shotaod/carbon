FROM openjdk:8-jre-alpine

VOLUME /tmp

RUN set -x && \
    apk update && \
    apk add nginx && \
    adduser -D -g 'www' www && \
    mv ./tmp/app.conf /etc/nginx/conf.d/ && \
    
