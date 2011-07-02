//**********************************************//
//**                                          **//
//**  qrcode.js                               **//
//**  QR Code button generator                **//
//**  © 2010 ben-robinson.com                 **//
//**                                          **//
//**  This code is free to use, copy, modify  **//
//**  and distribute - but must always        **//
//**  include this box.  				      **//
//**                                          **//
//**********************************************//

//***********************************************************************************
//** 
//** To add a QR Code button to your webage, create a <div></div> tag, with
//** an id value, then run the the createQRButton function.
//** 
//** createQRButton(pDiv, lbl, code)
//** 
//** 	pDiv	String	This is the id of the div that should contain the button
//** 					(can also be the dom element itself)
//** 
//** 	lbl		String	The label the button will display (or an empty string)
//** 
//** 	code	String	The string of text that is to be encoded into the QR code
//** 					i.e. a URL.
//**
//** Note: the function can be called as many times as required to change the code or label
//** Note: if your button is initially hidden, call this function after when you've displayed it.
//** 
//***********************************************************************************

function createQRButton(pDiv, lbl, code) {

	if (typeof(pDiv)=='string') {
		pDiv = document.getElementById(pDiv)
	}
	
	pDiv.innerHTML = '';
	
	pDiv.style.fontFamily = 'Helvetica, Arial, Sans';
	pDiv.style.fontSize = '14px';
	pDiv.style.display = 'inline-block';
	pDiv.style.cursor = 'pointer';
	pDiv.style.position = 'relative';
	
	pDiv.onmouseover = function() {
		if (this.closeQRCodeTimeout !== null) {
			clearTimeout(this.closeQRCodeTimeout);
		} else {
		
			var btnPos = null;
			var obj = this;
			if (obj.offsetParent) {
				var curleft = curtop = 0;
				do {
					curleft += obj.offsetLeft;
					curtop += obj.offsetTop;
				} while (obj = obj.offsetParent);
				btnPos = [curleft,curtop];
			}

			var windowS = null;
			if (window.innerHeight !=null) {
				windowS = [window.innerWidth, window.innerHeight];
			} else {
				if (document.documentElement && document.documentElement.clientHeight) {
					windowS = [document.documentElement.clientWidth, document.documentElement.clientHeight];
				} else if (document.body != null) {
					windowS = [document.body.clientWidth, document.body.clientHeight];
				}
			}
			
			sLeft = (document.documentElement.scrollLeft) ? document.documentElement.scrollLeft : document.body.scrollLeft;
			sTop = (document.documentElement.scrollTop) ? document.documentElement.scrollTop : document.body.scrollTop;
			
			var spaceAbove = btnPos[1] - sTop;
			var spaceBelow = windowS[1] - btnPos[1] + sTop;
			var spaceLeft = btnPos[0] - sLeft + this.offsetWidth;
			var spaceRight = windowS[0] - btnPos[0] + sLeft;
			
			var bigCode = this.childNodes[0];
			bigCode.style.top = (spaceBelow > spaceAbove) ? '23px' : '';
			bigCode.style.bottom = (spaceBelow > spaceAbove) ? '' : '23px';
			
			bigCode.style.left = (spaceRight > spaceLeft) ? '0px' : '';
			bigCode.style.right = (spaceRight > spaceLeft) ? '' : '1px';
			
//			var growToHeight = Math.min((spaceBelow > spaceAbove) ? spaceBelow-75 : spaceAbove-60, 350);
			var growToHeight = Math.min((spaceBelow > spaceAbove) ? spaceBelow-75 : spaceAbove-60, 350);
			var growToWidth = Math.min((spaceRight > spaceLeft) ? spaceRight-30 : spaceLeft-30, 350);
			this.growQRCodeToSize = Math.min(growToHeight, growToWidth);
			
			bigCode.style.height = '0px';
			bigCode.style.width = '0px';
			bigCode.style.display = 'block';
			enlargeQRCode(bigCode);	
		}
	};
	pDiv.onmouseout = function() {
		var timeId = new Date().valueOf()
		this.childNodes[0].id = 'qrCodeBtnImg' + timeId;
		this.closeQRCodeTimeout = setTimeout('closeQRCodeNOW(' + timeId + ')', 100);
	};
	pDiv.closeQRCodeTimeout = null;

	var img = document.createElement('img');
	img.src = 'http://chart.apis.google.com/chart?cht=qr&chs=350x350&chl=' + code;
	img.style.display = 'none';
	img.style.position = 'relative';
	img.style.border = '1px solid #999999';
	pDiv.appendChild(img);
	
	var div = document.createElement('div');
	div.style.height = '21px';
	div.style.width = '21px';
	div.style.cssFloat = div.style.styleFloat = 'left';
	div.style.background = 'url(data:image/gif;base64,R0lGODlhFQAVAKIFAO7u7ru7u+Pj4/n5+QAAAP///wAAAAAAACH5BAEAAAUALAAAAAAVABUAAAN3WLHc/iNGMmilVpJw8awfIVac6I0Whpao5pqkFJqevNHtiW9yP/aSAK2TOoGELyCORAAAcE+TM9oMRJ9OKZb2FF61zesUEBCYzwICGme+sUXt9FpQnqPlcjU9DVfT+HhmZSZ5emeEaoN9fGyAe3N+fox6Cg+WDwkAOw==) no-repeat';
	pDiv.appendChild(div);
	
	if (lbl != '') {
		var div = document.createElement('div');
		div.style.height = '17px';
		div.style.padding = '2px 6px';
		div.style.cssFloat = div.style.styleFloat = 'left';
		div.style.whiteSpace = 'nowrap';
		div.style.background = 'url(data:image/gif;base64,R0lGODlhAQAVAJEAALu7u+7u7uPj4/n5+SH5BAAAAAAALAAAAAABABUAAAIHxI4YK+0CCgA7) repeat-x';
		div.innerHTML = lbl;
		
		pDiv.appendChild(div);
		pDiv.style.width = (div.offsetWidth + 23) + 'px';
	}

	var div = document.createElement('div');
	div.style.height = '21px';
	div.style.width = '1px';
	div.style.cssFloat = div.style.styleFloat = 'left';
	div.style.background = 'url(data:image/gif;base64,R0lGODlhAQAVAIABALu7u////yH5BAEAAAEALAAAAAABABUAAAIFDI6pGQUAOw==) no-repeat';
	pDiv.appendChild(div);

	return pDiv;
}

function enlargeQRCode(img) {
	img.style.height = (parseInt(img.style.height)+10) + 'px';
	img.style.width = (parseInt(img.style.width)+10) + 'px';
	if (parseInt(img.style.height) < img.parentNode.growQRCodeToSize) {
		setTimeout(function() { enlargeQRCode(img) }, 10);
	}
}
function closeQRCodeNOW(timeId) {
	var img = document.getElementById('qrCodeBtnImg' + timeId)
	img.style.display = 'none';
	img.parentNode.closeQRCodeTimeout = null;
}