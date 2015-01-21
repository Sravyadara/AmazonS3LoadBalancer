
$(function() {

    //jvectormap data
    var visitorsData = {
        "US": 398, //USA
        "SA": 400, //Saudi Arabia
        "CA": 1000, //Canada
        "DE": 500, //Germany
        "FR": 760, //France
        "CN": 300, //China
        "AU": 700, //Australia
        "BR": 600, //Brazil
        "IN": 800, //India
        "GB": 320, //Great Britain
        "RU": 3000 //Russia
    };
    
    /*var countries = [
        {name: 'Singapore', coords: [1.30, 103.80], status: 'old'},
        {name: 'Tokyo', coords: [35.68, 139.69], status: 'new'},
        {name: 'Oregon', coords: [44.00,-120.50], status: 'new'},
        {name: 'N.California', coords: [38.28,-120.90], status: 'old'},
        {name: 'Ireland', coords: [53.34,6.26], status: 'new'},
        {name: 'Frankfurt', coords: [50.11,8.68], status: 'new'},
        {name: 'Sydney', coords: [-33.86,151.20], status: 'new'},
        {name: 'Sao Paulo', coords: [23.55,46.63], status: 'new'}
    ];*/
    
    var countries = (function() {
                var arr = []
                $.ajax({
                    url: "http://localhost:9000/compute/AmazonS3/DisplayRegions",
                    async: false,
                    success: function(result) {
                        $.each(result, function(k,v) {
                            $.each(v, function(k1,v1) {
                                if(k1 == "coords") {
                                    v1 = v1.split(",");
                                    result[k][k1] = v1;
                                }
                            arr.push(v);
                          });
                        });
                    } 
                });
                console.log(arr);
                return arr;
        })();
    
    $('#world-map').vectorMap({
    map: 'world_mill_en',
    backgroundColor: "transparent",
    scaleColors: ['#C8EEFF', '#0071A4'],
    normalizeFunction: 'polynomial',
    /*markerStyle: {
            initial: {
            fill: 'green',
            stroke: '#505050',
            r: 6
            }
        }, */
    markers: countries.map(function(h){ 
                            if(h.status == "old") { 
                                return {name: h.name, latLng: h.coords, style: {fill: 'red'}} 
                            }
                            else {
                                return {name: h.name, latLng: h.coords, style: {fill: 'yellow'}}
                            }
                        }),
    labels: {
        markers: {
          render: function(index){
            return countries[index].name;
          }
         
        }
    }
  });

});
