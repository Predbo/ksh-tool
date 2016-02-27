cmp = function(x, y){
    return x > y ? 1 : x < y ? -1 : 0; 
};

function sortSheetCurrent(data) {
	data.sort(function(a, b){
	    return cmp( 
	        [cmp(a.sheetNumber, b.sheetNumber), cmp(a.currentNumber, b.currentNumber)], 
	        [cmp(b.sheetNumber, a.sheetNumber), cmp(b.currentNumber, a.currentNumber)]
	    );
	});
}

function sortFamilyPrice(data) {
	data.sort(function(a, b){
	    return cmp( 
	        [cmp(a.familyNumber, b.familyNumber), cmp(a.price, b.price)], 
	        [cmp(b.familyNumber, a.familyNumber), cmp(b.price, a.price)]
	    );
	});
}

function getEuroPrice(price) {
	return euroPrice = parseFloat(price).toFixed(2).replace(".", ",") + " €";
}

$(function() {  
	setHeight();
    
});

$( window ).resize(function() {
    setHeight();
});

function setHeight() {
    $('.tab-content').height($(window).height() -150);
    //$('#offGrid').height($(window).height() -120);
}
