function httpAsync(theUrl, bodyObject, callback, method, loadingDiv, errorCallback){
	if(loadingDiv){
		var div = document.getElementById( loadingDiv );
		$(div).append('<img src="images/spinner.svg" style="width: 100%; height: 100%;"></img>');
	}
	var xmlhttp = new XMLHttpRequest();   // new HttpRequest instance 
	xmlhttp.onreadystatechange = function() { 
		if (xmlhttp.readyState == 4){
			if(loadingDiv){
				var div = document.getElementById(loadingDiv);
				$(div).children().last().remove();
			}
			if(xmlhttp.status == 200){
				callback(xmlhttp.responseText);
			}else{
				if( errorCallback ){
					errorCallback(xmlhttp.responseText);
				}else{
					alert('HTTP error ' + xmlhttp.status + " - " + xmlhttp.responseText);
				}
			}
		}
	}
	xmlhttp.open(method, theUrl, true);
	xmlhttp.timeout = 0; // time in milliseconds
	xmlhttp.setRequestHeader("Content-Type", "application/json");
	var body = bodyObject ? JSON.stringify( bodyObject ) :  null;
	xmlhttp.send( body );
}

function httpGetAsync(theUrl, callback, loadingDiv){
	httpAsync(theUrl, null, callback, 'GET', loadingDiv);
 }

function httpPostAsync(theUrl, bodyObject, callback, loadingDiv){
	httpAsync(theUrl, bodyObject, callback, 'POST', loadingDiv);
}

function httpPutAsync(theUrl, bodyObject, callback, loadingDiv){
	httpAsync(theUrl, bodyObject, callback, 'PUT', loadingDiv);
}

function httpDeleteAsync(theUrl, callback, loadingDiv){
	httpAsync(theUrl, null, callback, 'DELETE', loadingDiv);
}

function httpPostFile( propertyName, file, theUrl, callback, errorCallback){
	var formData = new FormData();
	formData.append(propertyName, file);

	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() { 
		if (xhr.readyState == 4){
			if( xhr.status == 200 ){
				callback(xhr.responseText);
			}else{
				if( errorCallback ){
					errorCallback(xhr.responseText);
				}else{
					alert('HTTP error ' + xhr.status + " - " + xhr.responseText);
				}
			}
		}
	}
	xhr.open("POST", theUrl);
	xhr.timeout = 0; // time in milliseconds
//	xhr.setRequestHeader("Content-Type", "multipart/form-data");
	xhr.send(formData);
}