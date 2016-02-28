var dataSet = []; // array von KshEntry Objekten
var maxCurrentNumber;


$(function() { 
	getDataSet();
	getMaxCurrentNumber();
	setHeight();
	drawDataEntryPage();
});

function getDataSet() {
	$.get("/data/entries", function(data) {
		$.each(data, function (index, value) {
			value.id = value.sheetNumber + "-" + value.currentNumber;
		});
		dataSet = data;
	}).fail(function() {
		alert("Applikation nicht korrekt gestartet");
	});
}

function getMaxCurrentNumber() {
	$.get("/config/max-current-number", function(data) {
		maxCurrentNumber = parseInt(data);
	}).fail(function() {
		alert("Konnte die Konfiguration nicht auslesen");
	});
}

function drawDataEntryPage() {
	$.get("tabs/data.html", function(data) {
	    $("#tab-content").html(data);
	});
}





cmp = function(x, y){
    return x > y ? 1 : x < y ? -1 : 0; 
};

function sortSheetCurrent() {
	dataSet.sort(function(a, b){
	    return cmp( 
	        [cmp(a.sheetNumber, b.sheetNumber), cmp(a.currentNumber, b.currentNumber)], 
	        [cmp(b.sheetNumber, a.sheetNumber), cmp(b.currentNumber, a.currentNumber)]
	    );
	});
}

function sortFamilyPrice() {
	dataSet.sort(function(a, b){
	    return cmp( 
	        [cmp(a.familyNumber, b.familyNumber), cmp(a.price, b.price)], 
	        [cmp(b.familyNumber, a.familyNumber), cmp(b.price, a.price)]
	    );
	});
}

function getEuroPrice(price) {
	return euroPrice = parseFloat(price).toFixed(2).replace(".", ",") + " €";
}

function setHeight() {
    $('.tab-content').height($(window).height() -150);
}


$( window ).resize(function() {
    setHeight();
});

function dummyGetReferenz() {
	$.get("/data/entries", function() {
		alert( "success" );
	}).done(function() {
		alert( "second success" );
	}).fail(function() {
		alert( "error" );
	}).always(function() {
		alert( "finished" );
	});
}
