"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var axios_1 = require("axios");
var apiKey = 'bb7f3363-01e1-40f5-b2b4-e474b0c5a8d9';
var apiUrl = 'https://api.hypixel.net/v2/recentgames';
axios_1.default.get(apiUrl, {
    params: {
        key: apiKey,
        uuid: '99590b37-3814-490b-849d-a6163b5d6810'
    }
})
    .then(function (response) {
    // Handle the response data here
    console.log(response.data);
})
    .catch(function (error) {
    // Handle any errors here
    console.error(error);
});
