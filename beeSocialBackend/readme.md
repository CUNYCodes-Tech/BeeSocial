# Get started with backend

- clone the repo
```
git clone https://github.com/CUNYCodes-Tech/BeeSocial.git

```
- go to directory beeSocialBackend
```
cd beeSocialBackend
```
- install dependencies
```
npm install
```
- make a data folder
```
mkdir data
```
- start up a mongodb server
```
mongod --dbpath data
```
- start the server in another terminal
```
npm start
```

# File structure
- *controller*: store necessary functions
- *middleware*: store middlewares
- *models*: store defined schemas
- *routes*: store the routers.