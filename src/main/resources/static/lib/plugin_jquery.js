
$(function () {

    $.fn.serializeJson = function () {
        var serializeObj = {};
        $(this.serializeArray()).each(function () {
            //if (this.value != "") {
            serializeObj[this.name] = this.value;
            //}
        });
        return serializeObj;
    };


    $.fn.serializeQueryString = function () {
        var str = "";
        $(this.serializeArray()).each(function () {

            if (str != "") {
                str += "&";
            }
            str += this.name + "=" + this.value;

        });
        return str;
    }

})
