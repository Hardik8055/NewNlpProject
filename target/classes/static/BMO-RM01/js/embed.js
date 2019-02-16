
window.onload = function() {

    $(window).resize(function() {
        var $iframes = $("iframe");
        $iframes.each(function() {
            var baseurl = $(this).attr('src');

            var isGraph = $(this).attr('src').indexOf("graph-landing");
            if (isGraph >= 0) {
                var width = $(this).parent().width();
                var height = ($(this).height());

                var height_factor = 25;
                if (width > 700) {
                    height_factor = 75;
                }

                var paramString = "&width=" + width + "&height=" + ($(this).height() - 35);
                var graphsource = baseurl;
                graphsource = graphsource.replace(/&width=.*&/, "&width=" + width + "&");
                graphsource = graphsource.replace(/&height=.*/, "&height=" + (height - height_factor));
                $(this).attr('src', graphsource);
            }
        });
    }).resize();
}
