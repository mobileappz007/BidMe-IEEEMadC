/*************************************************************************************/
// -->Template Name: Ample Admin
// -->Author: Themedesigner
// -->Email: niravjoshi87@gmail.com
// -->File: google_map_init
/*************************************************************************************/

$(function() {
    //******************************************//
    // Basic Map
    //******************************************//
    var map;
    map = new GMaps({
        div: '#map',
        lat: -12.043333,
        lng: -77.028333
    });

    //******************************************//
    // Map Events
    //******************************************//
    var map_1;
    map_1 = new GMaps({
        div: '#map_1',
        zoom: 16,
        lat: -12.043333,
        lng: -77.028333,
        click: function(e) {
            alert('click');
        },
        dragend: function(e) {
            alert('dragend');
        }
    });

    //******************************************//
    // Markers
    //******************************************//
    var map_2;
    map_2 = new GMaps({
        div: '#map_2',
        lat: -12.043333,
        lng: -77.028333
    });
    map_2.addMarker({
        lat: -12.043333,
        lng: -77.03,
        title: 'Lima',
        details: {
            database_id: 42,
            author: 'HPNeo'
        },
        click: function(e) {
            if (console.log)
                console.log(e);
            alert('You clicked in this marker');
        }
    });
    map_2.addMarker({
        lat: -12.042,
        lng: -77.028333,
        title: 'Marker with InfoWindow',
        infoWindow: {
            content: '<p>HTML Content</p>'
        }
    });

    //******************************************//
    // Polylines
    //******************************************//
    var map_3;
    map_3 = new GMaps({
        div: '#map_3',
        lat: -12.043333,
        lng: -77.028333,
        click: function(e) {
            console.log(e);
        }
    });

    path1 = [
        [-12.044012922866312, -77.02470665341184],
        [-12.05449279282314, -77.03024273281858],
        [-12.055122327623378, -77.03039293652341],
        [-12.075917129727586, -77.02764635449216],
        [-12.07635776902266, -77.02792530422971],
        [-12.076819390363665, -77.02893381481931],
        [-12.088527520066453, -77.0241058385925],
        [-12.090814532191756, -77.02271108990476]
    ];

    map_3.drawPolyline({
        path: path1,
        strokeColor: '#131540',
        strokeOpacity: 0.6,
        strokeWeight: 6
    });

    //******************************************//
    // Polygons
    //******************************************//
    var map_4;
    map_4 = new GMaps({
        div: '#map_4',
        lat: -12.043333,
        lng: -77.028333
    });

    var path2 = [
        [-12.040397656836609, -77.03373871559225],
        [-12.040248585302038, -77.03993927003302],
        [-12.050047116528843, -77.02448169303511],
        [-12.044804866577001, -77.02154422636042]
    ];

    polygon = map_4.drawPolygon({
        paths: path2,
        strokeColor: '#BBD8E9',
        strokeOpacity: 1,
        strokeWeight: 3,
        fillColor: '#BBD8E9',
        fillOpacity: 0.6
    });

    //******************************************//
    // Routes
    //******************************************//
    var map_5;
    map_5 = new GMaps({
        div: '#map_5',
        lat: -12.043333,
        lng: -77.028333
    });
    map_5.drawRoute({
        origin: [-12.044012922866312, -77.02470665341184],
        destination: [-12.090814532191756, -77.02271108990476],
        travelMode: 'driving',
        strokeColor: '#131540',
        strokeOpacity: 0.6,
        strokeWeight: 6
    });
});