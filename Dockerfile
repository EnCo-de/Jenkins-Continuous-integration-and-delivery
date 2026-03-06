FROM node:latest
WORKDIR /opt
ADD . /opt
RUN npm install
ENTRYPOINT npm run start
