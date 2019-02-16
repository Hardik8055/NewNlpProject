var myApp=angular.module('myApp', [])
.controller('custD', function($scope, $http) {
	 $scope.topics =[];
	 $scope.entitySend='';
	$scope.hid=0;
	
   // $http.get('/rmenable/Gtopic?row=50').
	$http.get('/rmenable/NewsTopic?row=50').
        then(function(response) {
          console.log(response.data);
        	$scope.data = response.data;
        	$scope.backup=response.data;
        	$scope.ent=$scope.data[0];
        	for (var i=0; i<$scope.data.length; i++) {
        		$scope.ent[i]=$scope.data[i];
        		 $scope.ent[i].selectedTopic=[];
        	    }

        	
        	$scope.ilen=$scope.data.length;
        	console.log($scope.data.length);
        });
  
      $scope.listold = function(){
    	  
    	  console.log("tell"+$scope.told);
      	if($scope.told !=null){
      		$scope.entitySend=$scope.ent.entity;
    	  $scope.ent.selectedTopic.push($scope.told);
    	  
    	  var ind=$scope.ent.topicList.indexOf($scope.told);
    	  console.log(ind+"tell"+ $scope.topics);
    	  $scope.ent.topicList.splice(ind,1);
      	}
       }
      $scope.listnew = function(){
    	  if($scope.tnew !=null){
    	  $scope.ent.topicList.push($scope.tnew);
    	  var ind=$scope.topics.indexOf($scope.tnew);
    	  $scope.ent.selectedTopic.splice(ind,1);
    	  }
       }
      $scope.reset = function(){
    	  $scope.topics =[];
    	  $scope.entitySend=$scope.ent.entity;
    	 
       }
      $scope.process = function(){
    	  console.log($scope.topics);
               $scope.tSearch = {
                       topic: $scope.ent.selectedTopic,
                       entity:$scope.entitySend
                   }
               console.log($scope.tSearch);
               var config = {
                   headers : {
                       'Content-Type': 'application/json;'
                   }
               }

               $http.post('/rmenable/topicSearch', $scope.tSearch, config).
                then(function(response) {
                	console.log(response.data);
                	$scope.article=response.data;
                },function(){
                	console.log("ERROR");
                });
          
    	 
       };
    
  
});

myApp.filter('unique', function() {
	   // we will return a function which will take in a collection
	   // and a keyname
	   return function(collection, keyname) {
	      // we define our output and keys array;
	      var output = [], 
	          keys = [];
	      
	      // we utilize angular's foreach function
	      // this takes in our original collection and an iterator function
	      angular.forEach(collection, function(item) {
	          // we check to see whether our object exists
	          var key = item[keyname];
	          // if it's not already part of our keys array
	          if(keys.indexOf(key) === -1) {
	              // add it to our keys array
	              keys.push(key); 
	              // push this item to our final output array
	              output.push(item);
	          }
	      });
	      // return our array which should be devoid of
	      // any duplicates
	      return output;
	   };
	});

