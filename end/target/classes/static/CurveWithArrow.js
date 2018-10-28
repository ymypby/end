/**
 * Created by ymy on 2018/10/20.
 */
function drawStation (point){
    var options = {
        fillColor:'#003399',
        fillOpacity:'1',
        strokeWeight:'0px',
        strokeOpacity:'0'

    };
    var circle  = new BMap.Circle(point,70,options);
    return circle;
}

function drawStations(points){
    var circles = [];
    for(var i=0;i<points.length;i++){
        circles.push(drawStation(points[i]));
    }
    return circles;
}

function drawCurveWithArrow(startPoint,endPoint,color,weight,scale){
    var curveOptions = {
        strokeColor:color,
        strokeWeight:weight,
        strokeOpacity:1
    };
    var curve = new BMapLib.CurveLine([startPoint,endPoint], curveOptions);
    var points = curve.getPath();
    var k = (points[16].lng-points[17].lng)/(points[16].lat-points[17].lat);
    var vectorBCArrow = new BMap.Marker(points[17], {
        // 初始化方向向下的闭合箭头并旋转一定的角度

        icon: new BMap.Symbol(BMap_Symbol_SHAPE_BACKWARD_OPEN_ARROW, {
            scale: scale,
            strokeColor: color,
            strokeOpacity:0,
            rotation: Math.atan(k)*180/Math.PI,
            fillColor: color,
            fillOpacity: 1
        })
    });
    return {
        'curve':curve,
        'vectorBCArrow':vectorBCArrow
    };
}

function drawMustPoints(dataset,stationsMap){
    var points = [];
    var points1 = dataset[4];
    for(var i=0;i<points1.length;i++){
        points.push(new BMap.Point(stationsMap[points1[i]].baidu_X,stationsMap[points1[i]].baidu_Y));
    }
    return drawStations(points);
}
function drawCurves(dataset,stationsMap){
    var samples = dataset[3];
    var count = [];
    for(var i=0;i<samples.length;i++){
        count.push(samples[i].count);
    }
    var min = d3.min(count);
    var max = d3.max(count);
    d3.select('#min').text(min);
    d3.select('#max').text(max);
    console.log(min);
    console.log(max);
    var colorScale = d3.scaleLinear().domain([min, max]).range(["green", "yellow","red"]);
    var weightScale = d3.scaleLinear().domain([min,max]).range([1,10]);
    //var trianScale = d3.scaleLinear().domain([min,max]).range([1.2,3]);
    var curves = [];
    var curve;
    var point1;
    var point2;
    var color;
    var arrow;
    var arrows = [];
    for(var i=0;i<samples.length;i++){
        point1 = new BMap.Point(stationsMap[samples[i].leaseStation].baidu_X,stationsMap[samples[i].leaseStation].baidu_Y);
        point2 = new BMap.Point(stationsMap[samples[i].returnStation].baidu_X,stationsMap[samples[i].returnStation].baidu_Y);
        color = colorScale(samples[i].count);
        weight = weightScale(samples[i].count);
        //scale = trianScale(samples[i].count);
        // scale = 1.3;
        // curve = drawCurveWithArrow(point1,point2,color,weight,scale);
        var curveOptions = {
            strokeColor:color,
            strokeWeight:weight,
            strokeOpacity:1
        };
        var polygonOptions={
            strokeOpacity:1,
            strokeWeight:1,
            strokeColor:color,
            fillColor:color,
            fillOpacity:1
        };
        curve = new BMapLib.CurveLine([point1,point2], curveOptions);
        arrow = addArrow(curve,polygonOptions);
        curves.push(curve);
        arrows.push(arrow);
    }
    return [curves,arrows];
}

function addArrow(lines,line_style) {
    var length = 14;
    var angleValue = Math.PI/7;
    var linePoint = lines.getPath();
    var arrowCount = linePoint.length;
    var middle = arrowCount / 2;
    var pixelStart = map.pointToPixel(linePoint[Math.floor(middle)]);
    var pixelEnd = map.pointToPixel(linePoint[Math.ceil(middle)]);
    var angle = angleValue;
    var r = length;
    var delta = 0;
    var param = 0;
    var pixelTemX, pixelTemY;
    var pixelX, pixelY, pixelX1, pixelY1;
    if (pixelEnd.x - pixelStart.x == 0) {
        pixelTemX = pixelEnd.x;
        if (pixelEnd.y > pixelStart.y) {
            pixelTemY = pixelEnd.y - r;
        } else {
            pixelTemY = pixelEnd.y + r;
        }
        pixelX = pixelTemX - r * Math.tan(angle);
        pixelX1 = pixelTemX + r * Math.tan(angle);
        pixelY = pixelY1 = pixelTemY;
    } else {
        delta = (pixelEnd.y - pixelStart.y) / (pixelEnd.x - pixelStart.x);
        param = Math.sqrt(delta * delta + 1);
        if ((pixelEnd.x - pixelStart.x) < 0) {
            pixelTemX = pixelEnd.x + r / param;
            pixelTemY = pixelEnd.y + delta * r / param;
        } else {
            pixelTemX = pixelEnd.x - r / param;
            pixelTemY = pixelEnd.y - delta * r / param;
        }
        pixelX = pixelTemX + Math.tan(angle) * r * delta / param;
        pixelY = pixelTemY - Math.tan(angle) * r / param;
        pixelX1 = pixelTemX - Math.tan(angle) * r * delta / param;
        pixelY1 = pixelTemY + Math.tan(angle) * r / param;
    }
    var pointArrow = map.pixelToPoint(new BMap.Pixel(pixelX, pixelY));
    var pointArrow1 = map.pixelToPoint(new BMap.Pixel(pixelX1, pixelY1));
    var  Arrow = new BMap.Polygon([pointArrow, linePoint[Math.ceil(middle)], pointArrow1],line_style);
    // var Arrow = new BMap.Polyline([pointArrow, linePoint[Math.ceil(middle)], pointArrow1], line_style);
    return Arrow;
}
