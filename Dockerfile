FROM node:7.8.0
WORKDIR /opt
ADD . /opt
RUN echo echo
RUN npm install
ENTRYPOINT npm run start
