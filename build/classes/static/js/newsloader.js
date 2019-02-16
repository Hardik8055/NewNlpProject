var localMode = true;

function load() {
    var mydata = JSON.parse(data);
    var selectEntity = document.getElementById('populateScale');

    for (var i = 0; i < mydata.length; i++) {
        var topics = null;
        // selectEntity.add(new Option(mydata[i].entity));
        var option = document.createElement('option');
        option.text = mydata[i].entity;
        option.value = mydata[i].topic;
        selectEntity.add(option, 1);
    }
}


function displayLocalJSONNews() {
    var entityObj = document.getElementById('populateScale');
    var topicObj = document.getElementById('populateTopic');
    var link = "json/" + entityObj.value + "/" + topicObj.value + ".json";
    $.getJSON(link, function(mydata) {
        console.log("Data:", mydata);
        createNews(mydata);
    });
}

function displayLocalJSONEntityNews() {
    var entityObj = document.getElementById('populateScale');
    var topicObj = document.getElementById('populateTopic');
    var link = "json/" + entityObj.value + "/" + entityObj.value + ".json";
    $.getJSON(link, function(mydata) {
        console.log("Data:", mydata);
        createEnityNews(mydata);
    });
}

function createEnityNews(mydata) {
    var length = mydata.articles.length;
    for (var i = 0; i < mydata.articles.length; i++) {
        var elm = '<div class="row w-100 border-bottom pb-2 mb-3"><div class="col-4 w-100"><img class = "  rounded loading"src="http://api.rethumb.com/v1/square/150/' +
            mydata.articles[i].urlToImage +
            '"' +'></div><div class="col-8 w-100"><div class="row w-100 ">' +
            '<p class="article-title" id="Title1"><b>"' +
            mydata.articles[i].title +'"</b></p>' +'</div>' + '<div class="row w-100 mt-2 align-bottom"><p class="mr-5 mb-0 mt-2"> <a class ="feedback-button-like"><i class="fas fa-thumbs-up fa-lg"></i> Like </a></p> <p class="mr-5 mb-0 mt-2"> <a class ="feedback-button-dislike" href="#"><i class="fas fa-thumbs-down"></i> Dislike </a> </p> <a class="btn-default-new pull-right" href="' + mydata.articles[i].url + '">Read More</a></div></div></div>';
        $('#entity').append(elm);
    }
}

function createNews(mydata) {
    var length = mydata.articles.length;
    for (var i = 0; i < mydata.articles.length; i++) {
        var elm = '<div class="row w-100 border-bottom pb-2 mb-3"><div class="col-4 w-100"><img class = " rounded loading" src="http://api.rethumb.com/v1/square/150/' +
            mydata.articles[i].urlToImage + '" ' + '></div><div class="col-8 w-100"><div class="row w-100">' +
            ' <p class="article-title" id="Title1"><b>"' +
            mydata.articles[i].title +
            '"</b></p>' +
            '</div>' +
            '<div class="row w-100 mt-2"><p class="mr-5 mb-0 mt-2 text-center"> <a class ="feedback-button-like" href="#"> <i class="fas fa-thumbs-up"></i> Like </a> </p><p class="mr-5 mb-0 mt-2">  <a class ="feedback-button-dislike" href="#"><i class="fas fa-thumbs-down"></i> Dislike </a></p> <a class="btn-default-new pull-right" href="' + mydata.articles[i].url + '">Read More</a></div></div></div>';
        $('#industry').append(elm);
    }
}

function selectTopic() {
    var selectedEntity = document.getElementById("populateScale").value;
    var mydata = JSON.parse(data);
    var map = new Object();
    document.getElementById('populateTopic').options.length = 0;
    document.getElementById('populateTopic').add(new Option("Select Value"));
    for (var i = 0; i < mydata.length; i++) {
        var topics = null;
        var selectTopic = document.getElementById('populateTopic');
        if (selectedEntity == mydata[i].topic) {
            var topicList = mydata[i].topicList;
            for (var j = 0; j < topicList.length; j++) {
                var option = document.createElement('option');
                option.text = topicList[j].value;
                option.value = topicList[j].Topic;
                selectTopic.add(option, 1);
            }
        }
    }
}

function displayNews() {
    $('#industry').html('');
    $('#entity').html('');
    var newsHeader = '<div class="row w-100"><p class="news-title mt-2 pl-3">Industry - News</p></div>';
    $('#industry').append(newsHeader);
    var newsHeader = '<div class="row w-100"><p class="news-title mt-2 pl-3" id="entity-title" >Entity - News</p></div>';
    $('#entity').append(newsHeader);
    var entityObj = document.getElementById('populateScale');
    var topicObj = document.getElementById('populateTopic');
    if (entityObj.value === "cbiz" || entityObj.value === "green") {
			$('#entity-title').css("visibility", "visible");
        if (localMode) {
            displayLocalJSONEntityNews();
        } else {
            displayEntityNews();
        }
    }
		else {
			$('#entity-title').css('visibility','hidden');
		}
    if (localMode) {
        displayLocalJSONNews();
    } else {
        var link = "http://localhost:8085/rmenable/BMO-RM01/json/" + entityObj.value + "/" + topicObj.value + ".json";
        $.ajax({
            type: 'GET',
            url: link,
            data: {
                get_param: 'value'
            },
            crossDomain: true,
            headers: {
                "accept": "application/json",
                "Access-Control-Allow-Origin": "*"
            },
            dataType: 'json',
            success: function(mydata) {
                console.log("Data:", mydata);
                createNews(mydata);
            }
        });
    }
}

function displayEntityNews() {
    var entityObj = document.getElementById('populateScale');
    var topicObj = document.getElementById('populateTopic');
    var link = "http://localhost:8085/rmenable/BMO-RM01/json/" + entityObj.value + "/" + entityObj.value + ".json";
    $.ajax({
        type: 'GET',
        url: link,
        data: {
            get_param: 'value'
        },
        crossDomain: true,
        headers: {
            "accept": "application/json",
            "Access-Control-Allow-Origin": "*"
        },
        dataType: 'json',
        success: function(mydata) {
            console.log("Data:", mydata);
            createEnityNews(mydata);
        }
    });
}
