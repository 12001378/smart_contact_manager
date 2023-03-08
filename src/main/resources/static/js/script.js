const toogleSidebar = () => {

	if ($('.sidebar').is(':visible')) {

		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0");

	} else {

		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
		$(".content .fas").css("display", "none");

	}
}

//search functionality

const search = () => {
	// console.log("searching....")

	let query = $("#search-input").val();
	console.log(query);

	if (query=="") {
		$(".search-result").hide();

	} else {
        //sending request to server

        let url = `http://localhost:8080/search/${query}`;

        fetch(url).then((Response) => {
            return Response.json();
        })
        .then((data) =>{
          

            let text =`<div class='list-group'>`;

            data.forEach((contact) => {
                text+=`<a href='/user/${contact.cid}/contact' class="list-group-item list-group-item-action">${contact.name}</a>`
            });

            text+=`</div>`;
            $(".search-result").html(text);
            $(".search-result").show();
        });
		$(".search-result").show();
	}
};

