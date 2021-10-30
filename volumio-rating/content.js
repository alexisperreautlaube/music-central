var titleObserver = new MutationObserver(function(mutations) {
  mutations.forEach(function(mutation) {
    console.log("volumio title mutation :" + mutation);
    var rating = fetchRating();
    setRating(rating);
  })
})

var bodyObserver = new MutationObserver(function(mutations) {
  mutations.forEach(function(mutation) {
    if (!mutation.addedNodes) return;
    for (var i = 0; i < mutation.addedNodes.length; i++) {

      var node = mutation.addedNodes[i]
      if (typeof node.classList !== "undefined" && node.id == 'trackInfo-title'){
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

function setRating(rating) {
  console.log("setRating :" + rating)
  let byId = document.getElementById('trackInfo-title');
  byId.textContent = byId.textContent + " - " + rating;
}


function fetchRating(){
  return 4;
}



bodyObserver.observe(document.body, {
  childList: true
  , subtree: true
  , attributes: false
  , characterData: false
})
console.log("volumio star rating loaded");

