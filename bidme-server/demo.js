var options = {
  host: 'https://api.ravepay.co/v2/gpx/subaccounts/create',
  port: 80,
  path: '/',
  method: 'POST'
};

http.request(options, function(res) {
  console.log('STATUS: ' + res.statusCode);
  console.log('HEADERS: ' + JSON.stringify(res.headers));
  res.setEncoding('utf8');
  var data={
    Subaccounts Name: "Noah Alorwu",
Subaccounts email:"ot.clement@gmail.com",
Subaccounts Country: "Ghana",
Bank Name: "Access Bank Ghana LTD",
Account Number:"0051614743401",
  seckey: "FLWSECK-5bbddffae0f14a462e3afbffb85ba351-X"
  }
  res.on('data', function (chunk) {
    console.log('BODY: ' + chunk);
  });
}).end();