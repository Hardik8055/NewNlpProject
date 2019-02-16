const canvasWidth = 725;
const canvasHeight = 350;
$(document).ready(function() {
  var currentCanvas;

  //line chart starts
  currentCanvas = document.getElementById("chart-1");
  currentCanvas.width = canvasWidth;
  currentCanvas.height = canvasHeight;
  var ctx = currentCanvas.getContext("2d");

  const colors = {
    blue: {
      fill: '#127ABF',
      stroke: '#127ABF',
      opacity : '100%',
    },

    red: {
      fill: '#e0eadf',
      stroke: '#5eb84d',
      opacity : '100%',
    },

    bluegrad: {
        fill: '#6fccdd'

      },

  };

  const loggedIn = [125, 20, 100, 70,100];

  const availableForExisting = [80, 50 , 90 , 110, 85];

  const xData = ["0", "Q417" , "Q118" , "Q218" , "Q318"];

  const myChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: xData,
      datasets: [ {
        label: "New Cars",
        fill: true,
        backgroundColor: colors.bluegrad.fill,
        pointBackgroundColor: colors.blue.stroke,
        borderColor: colors.blue.stroke,
        pointHighlightStroke: colors.blue.stroke,
        borderCapStyle: 'line',
        data: availableForExisting,
      },
      {
        label: "Old Cars",
        fill: true,
        backgroundColor: colors.red.fill,
        pointBackgroundColor: colors.red.stroke,
        borderColor: colors.red.stroke,
        pointHighlightStroke: colors.red.stroke,
        data: loggedIn,
      }]
    },
    options: {
      responsive: false,
      bezierCurve: false,
      // Can't just just `stacked: true` like the docs say
      scales: {
        yAxes: [{
          stacked: true,
        }]
      },
      animation: {
        duration: 750,
      },
      legend: {
          position: 'right' // place legend on the right side of chart
       },
    }
  });
  //line chart ends
  $('#chart1-tab').on('click', function() {


    //line chart starts
    var ctContext = currentCanvas.getContext('2d');
    ctContext.clearRect(0, 0, currentCanvas.width, currentCanvas.height);

    currentCanvas = document.getElementById("chart-1");
    currentCanvas.width = canvasWidth;
    currentCanvas.height = canvasHeight;
    var ctx = currentCanvas.getContext("2d");

    const colors = {
      blue: {
        fill: '#127ABF',
        stroke: '#127ABF',
        opacity : '100%',
      },

      red: {
        fill: '#e0eadf',
        stroke: '#5eb84d',
        opacity : '100%',
      },

      bluegrad: {
          fill: '#6fccdd'

        },

    };

    const loggedIn = [125, 20, 100, 70,100];

    const availableForExisting = [80, 50 , 90 , 110, 85];

    const xData = ["0", "Q417" , "Q118" , "Q218" , "Q318"];

    const myChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: xData,
        datasets: [ {
          label: "New Cars",
          fill: true,
          backgroundColor: colors.bluegrad.fill,
          pointBackgroundColor: colors.blue.stroke,
          borderColor: colors.blue.stroke,
          pointHighlightStroke: colors.blue.stroke,
          borderCapStyle: 'line',
          data: availableForExisting,
        },
        {
          label: "Old Cars",
          fill: true,
          backgroundColor: colors.red.fill,
          pointBackgroundColor: colors.red.stroke,
          borderColor: colors.red.stroke,
          pointHighlightStroke: colors.red.stroke,
          data: loggedIn,
        }]
      },
      options: {
        responsive: false,
        bezierCurve: false,
        // Can't just just `stacked: true` like the docs say
        scales: {
          yAxes: [{
            stacked: true,
          }]
        },
        animation: {
          duration: 750,
        },
        legend: {
            position: 'right' // place legend on the right side of chart
         },
      }
    });
    //line chart ends
  })
  $('#chart2-tab').on('click', function() {
    // Rangebar Chart
    var ctContext = currentCanvas.getContext('2d');
    ctContext.clearRect(0, 0, currentCanvas.width, currentCanvas.height);

    var chart0 = new CanvasJS.Chart("chartContainer", {
      animationEnabled: true,
      title: {
        text: "Number of Days"
      },
      axisX: {
        title: ""
      },
      axisY: {
        includeZero: false,
        color : ["EA212D" , "#95242C" ],
        interval: 100
      },
      data: [{
        type: "rangeBar",
        yValueFormatString: "#0",
        indexLabel: "{y[#index]}",
        legendText: "",
        toolTipContent: "<b>{label}</b>: {y[0]} to {y[1]}",


       dataPoints: [
                      { x: 10,  y:[200, -250 ],  label: "Q4" },
                  { x: 20,  y:[270, -270], label: "Q3" },
                  { x: 30, y:[10, -10], label: "Q2" },
                  { x: 40, y:[90, -160], label: "Q1" }
                    ]

      }]
    });
    chart0.render();
    // Rangebar chart
  })
  $('#chart3-tab').on('click', function() {
    //stacked bar chart starts
    var ctContext = currentCanvas.getContext('2d');
    ctContext.clearRect(0, 0, currentCanvas.width, currentCanvas.height);
    currentCanvas = document.getElementById("chart-3");

    currentCanvas.width = canvasWidth;
    currentCanvas.height = canvasHeight;

    var ctx2 = currentCanvas.getContext("2d");
    var chart2 = new Chart(ctx2, {
    type: 'bar',
    data: {
       labels: ['Q4-17', 'Q1-18', 'Q2-18', 'Q3-18'], // responsible for how many bars are gonna show on the chart
       // create 12 datasets, since we have 12 items
       // data[0] = labels[0] (data for first bar - 'Standing costs') | data[1] = labels[1] (data for second bar - 'Running costs')
       // put 0, if there is no data for the particular bar
       datasets: [
         {
          label: '0-30 Days',
          data: [25.32, 25.32 , 93.27 , 1],
          backgroundColor: '#269D1C'
       },
       {
          label: '31-60 Days',
          data: [98.68, 50 , 31.04, 93.27],
          backgroundColor: '#EFB74E'
       },
       {
          label: '> 60 Days',
          data: [31.25, 78.25, 31.04 , 24.73],
          backgroundColor: '#127ABF'
       }
       ]
    },
    options: {
       responsive: false,
       legend: {
          position: 'bottom' // place legend on the right side of chart
       },
       scales: {
          xAxes: [{
             stacked: true,

             barPercentage: 0.5
             // this should be set to make the bars stacked
          }],
          yAxes: [{
             stacked: true // this also..
          }]
       }
    }
    });
    // statcked bar chart ends
  })
  $('#chart4-tab').on('click', function() {
    // Grouped bar chart
    var ctContext = currentCanvas.getContext('2d');
    ctContext.clearRect(0, 0, currentCanvas.width, currentCanvas.height);

    currentCanvas = document.getElementById("chart-4");
    currentCanvas.width = canvasWidth;
    currentCanvas.height = canvasHeight;

    var ctx3 = currentCanvas.getContext("2d");

    var data = {
        labels: ["Q4-17", "Q1-18", "Q2-18" , "Q3-18" , "Q4-17", "Q1-18", "Q2-18" , "Q3-18" ],
        datasets: [
            {
                label: "ABINC",
                backgroundColor: "#127ABF",
                data: [70.27,10,40,60,15,140,50,70]
            },
            {
                label: "Peer2",
                backgroundColor: "#EFB74E",
                data: [128.22,83.6,60,175,125,75,25,100]
            },
            {
                label: "Peer3",
                backgroundColor: "#269D1C",
                data: [40,60,5,30,65,45,10,50]
            }
        ]
    };

    var myBarChart = new Chart(ctx3, {
        type: 'bar',
        data: data,
        options: {

        	legend: {
                position: 'bottom' // place legend on the right side of chart
             },

            barValueSpacing: 20,
            scales: {
                yAxes: [{
                    ticks: {
                        min: 0,
                    }
                }]
            }
        }
    });
    // Grouped bar chart
  })
  $('#chart5-tab').on('click', function() {
    // Grouped bar chart
    var ctContext = currentCanvas.getContext('2d');
    ctContext.clearRect(0, 0, currentCanvas.width, currentCanvas.height);

    currentCanvas = document.getElementById("chart-5");
    currentCanvas.width = canvasWidth;
    currentCanvas.height = canvasHeight;

    var ctx4 = currentCanvas.getContext("2d");

    var data = {
        labels: ["Q4-17", "Q1-18", "Q2-18" , "Q3-18" , "Q4-17", "Q1-18", "Q2-18" , "Q3-18" ],
        datasets: [
            {
                label: "ABINC",
                backgroundColor: "#127ABF",
                data: [70.27,10,40,60,15,140,50,70]
            },
            {
                label: "Peer2",
                backgroundColor: "#EFB74E",
                data: [128.22,83.6,60,175,125,75,25,100]
            },
            {
                label: "Peer3",
                backgroundColor: "#269D1C",
                data: [40,60,5,30,65,45,10,50]
            }
        ]
    };

    var myBarChart = new Chart(ctx4, {
        type: 'bar',
        data: data,
        options: {

        	legend: {
                position: 'bottom' // place legend on the right side of chart
             },

            barValueSpacing: 20,
            scales: {
                yAxes: [{
                    ticks: {
                        min: 0,
                    }
                }]
            }
        }
    });
    // Grouped bar chart
  })

  // multiple charts

  var chart = new CanvasJS.Chart("chartContainer1",
      {
          animationEnabled: true,
          title: {
              text: "Revenue",
              fontSize: 14
          },
          height: 260,
          width: 300,
          axisX: {
              interval: 10,
          },
          data: [
          {
              type: "column",
              color: "#EFB74E",
              dataPoints: [
                  { x: 10, y: 125, label: "Q417" },
                  { x: 20, y: 90, label: "Q118" },
                  { x: 30, y: 60, label: "Q218" },
                  { x: 40, y: 175, label: "Q318" }

              ]
          },
          ]
      });

  chart.render();

  var chart1 = new CanvasJS.Chart("chartContainer2",
  	    {
  	        animationEnabled: true,
  	        title: {
  	            text: "Gross Profit",
                fontSize: 14
  	        },
  	        axisX: {
  	            interval: 10,
  	        },
            height: 260,
            width: 300,
  	        data: [
  	        {
  	            type: "column",
  	            color: "#127ABF",
  	            dataPoints: [
  	            	 { x: 10, y: 90, label: "Q417" },
  	                 { x: 20, y: 40, label: "Q118" },
  	                 { x: 30, y: 50, label: "Q218" },
  	                 { x: 40, y: 125, label: "Q318" }
  	            ]
  	        },
  	        ]
  	    });

  	chart1.render();

  	var chart2 = new CanvasJS.Chart("chartContainer3",
  		    {
  		        animationEnabled: true,
  		        title: {
  		            text: "Operating Expense",
                  fontSize: 14
  		        },
              height: 260,
              width: 300,
  		        axisX: {
  		            interval: 10,
  		        },
  		        data: [
  		        {
  		            type: "column",
  		            color: "#A8A6A6",
  		            dataPoints: [
  		            	 { x: 10, y: 110, label: "Q417" },
  		                 { x: 20, y: 180, label: "Q118" },
  		                 { x: 30, y: 60, label: "Q218" },
  		                 { x: 40, y: 175, label: "Q318" }
  		            ]
  		        },
  		        ]
  		    });

  		chart2.render();

  		var chart3 = new CanvasJS.Chart("chartContainer4",
  			    {
  			        animationEnabled: true,
  			        title: {
  			            text: "EPIDTA",
                    fontSize: 14
  			        },
                height: 260,
                width: 300,
  			        axisX: {
  			            interval: 10,
  			            barPercentage: 0.2
  			        },
  			        data: [
  			        {
  			            type: "column",
  			            color: "#12B2BF",
  			            dataPoints: [
  			            	 { x: 10, y: 65, label: "Q417" },
  			                 { x: 20, y: 10, label: "Q118" },
  			                 { x: 30, y: 40, label: "Q218" },
  			                 { x: 40, y: 100, label: "Q318" }
  			            ]
  			        },
  			        ]
  			    });

  			chart3.render();


  			var chart4 = new CanvasJS.Chart("chartContainer5",
  				    {
  				        animationEnabled: true,
  				        title: {
  				            text: "Net Income",
                      fontSize: 14
  				        },
  				        axisX: {
  				            interval: 10,
  				        },
                  height: 260,
                  width: 300,
  				        data: [
  				        {


  				        	type : "area",
  				            fillOpacity: .3,
  				            type: "column",
  				            color: "#95242C",
  				            dataPoints: [
  				            	 { x: 10, y: 25, label: "Q417" },
  				                 { x: 20, y: 10, label: "Q118" },
  				                 { x: 30, y: 15, label: "Q218" },
  				                 { x: 40, y: 70, label: "Q318" }
  				            ]
  				        },
  				        ]
  				    });

  				chart4.render();

  				var chart5 = new CanvasJS.Chart("chartContainer6",
  					    {
  					        animationEnabled: true,
  					        title: {
  					            text: "Other Income",
                        fontSize: 14
  					        },
                    height: 260,
                    width: 300,
  					        axisX: {
  					            interval: 10,
  					        },
  					        data: [
  					        {
  					            type: "column",
  					            color: "#269D1C",
  					            dataPoints: [
  					            	 { x: 10, y: 125, label: "Q417" },
  					                 { x: 20, y: 90, label: "Q118" },
  					                 { x: 30, y: 60, label: "Q218" },
  					                 { x: 40, y: 10, label: "Q318" }
  					            ]
  					        },
  					        ]
  					    });

  					chart5.render();
  				//	}
  // multiple charts

              $.ajax({
                  type: 'GET',
                  url: 'http://localhost:8085/rmenable/newsfner',
                  data: { get_param: 'value' },
                  crossDomain: true,
                  headers: {
                      "accept": "application/json",
                          "Access-Control-Allow-Origin":"*"
                  },
                  dataType:'json',
                  success: function (data) {
                                      var names = data
                                      console.log("data values", data);
                                      $("#newsTitle1").text(data.articles[0].title);
                                      $("#newsTitle2").text(data.articles[1].title);
                  }
              });

              $.ajax({
                  type: 'GET',
                  url: 'http://localhost:8085/rmenable/findImpact',
                  data: { get_param: 'value' },
                  crossDomain: true,
                  headers: {
                      "accept": "application/json",
                          "Access-Control-Allow-Origin":"*"
                  },
                  dataType:'json',
                  success: function (data) {
                                      console.log("data values", data);
                                      $("#impactTitle1").text("Dealer "+data.impacts[0].office+" sold"+data.impacts[0].count+" units of"+data.impacts[0].brand+" "+data.impacts[0].model+" from year"+data.impacts[0].from+" to"+data.impacts[0].to);
                                      $("#impactTitle2").text("Dealer "+data.impacts[1].office+" sold"+data.impacts[1].count+" units of"+data.impacts[1].brand+" "+data.impacts[1].model+" from year"+data.impacts[1].from+" to"+data.impacts[1].to);
                  }
              });
});
