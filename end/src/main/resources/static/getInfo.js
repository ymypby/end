/**
 * Created by ymy on 2018/10/13.
 */
var INFO = window.INFO = INFO || {};
(function(){
    INFO.AjaxInfo = AjaxInfo;
    function AjaxInfo() {
        var self = this;
        this.data = [] ;
        this.postData = function(url,data1) {
            $.ajax({
                url: url,
                contentType: 'application/json;charset=UTF-8',
                async: false,
                data: JSON.stringify(data1),
                type: 'post',
                accept: 'application/json',
                dataType: 'json',
                success: function (data) {
                    self.data = data;
                },
                error: function () {
                    window.alert("error");
                }
            });
            return this.data;
        };

        this.getData = function(url) {
            $.ajax({
                url: url,
                // contentType: 'application/json;charset=UTF-8',
                async: false,
                // data: JSON.stringify(data1),
                type: 'get',
                accept: 'application/json',
                dataType: 'json',
                success: function (data) {
                    self.data = data;
                },
                error: function () {
                    window.alert("error");
                }
            });
            return this.data;
        }
    }
})();