function callFCallback(fbResponse, provider) {
//        console.log(fbResponse);
    var url = 'http://fanoutings.com/user/' + provider;
    var method = 'POST';

    $.ajax({
        type: method,
        url: url,
        data: fbResponse,
        success: function (res) {
//                console.log(res);
            showInterests();
        }
    });

}
