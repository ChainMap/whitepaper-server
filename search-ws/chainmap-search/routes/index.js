var express = require('express');
var SolrNode = require('solr-node');
var router = express.Router();


var client = new SolrNode({
    host: 'localhost',
    port: '8983',
    core: 'chainmap',
    protocol: 'http',
    debugLevel: 'ERROR' // log4js debug level paramter 
});

var strQuery = client.query().q('text:test');
var objQuery = client.query().q({text:'test', title:'test'});
var myStrQuery = 'q=text:test&wt=json';


router.get('/paper', function(req, res, next) {
  // var query = client.query().q('content:bitcoin');
  var query = 'q=content:bitcoin&fl=file_name&wt=json';
  client.search(query, function(err, obj) {
  	if(err) {
  		res.send(err);
  	} else {
  		res.json(obj);
  	}
  
  });
});



/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

module.exports = router;
