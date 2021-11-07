var lastTrackTitle = "";
var titleObserver = new MutationObserver(function(mutations) {
  mutations.forEach(function(mutation) {
    console.log("volumio title mutation :" + mutation);
    window.fetch("http://localhost:8080/mc/rating",
        {
            method:"get",
            headers: {'Access-Control-Allow-Origin': '*'}
        })
        .then(function(response) {
          return response.json();
        }).then(function(data) {
            console.log("rating: " + data);
            console.log("setRating :" + rating)
            let byId = document.getElementById('trackInfo-title');
            lastTrackTitle = byId.textContent + " - " + data;
            byId.textContent = lastTrackTitle;
        }).catch(function(e ) {
          console.log("e:" + e);
        });
  })
})

var bodyObserver = new MutationObserver(function(mutations) {
  mutations.forEach(function(mutation) {
    if (!mutation.addedNodes) return;
    for (var i = 0; i < mutation.addedNodes.length; i++) {

      var node = mutation.addedNodes[i]
      if (typeof node.classList !== "undefined" && node.id == 'trackInfo-title' && node.textContent != lastTrackTitle){
        console.log("volumio found controls");
        titleObserver.observe(node, {
          childList: false
          , subtree: true
          , attributes: false
          , characterData: true
        })
      }
    }
  })
})

bodyObserver.observe(document.body, {
  childList: true
  , subtree: true
  , attributes: false
  , characterData: false
})
console.log("volumio star rating loaded");

