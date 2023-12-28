<%@ include file="/Layout/header.jsp" %>
	<title>Bulletin Board</title>
	<div id="content">
	 <div class="flex-1">
    <section>
      <div class="container px-4">
        <div class="row">
          <div class="col-6 d-flex justify-content-center flex-column">
            <h1 class="fw-bold text-left">
              Welcome to our <br> Bulletin Board
            </h1>
            <p>
              We provide the best service. Join us today and enjoy the benefits.
            </p>
            <a
              class="startBtn"
              href="<%= request.getContextPath() %>"
            >
              Get Started
            </a>
          </div>
        	<img
            src="<%= request.getContextPath() + "/assets/img/img_landing.jpg" %>"
            alt="Hero"
            class="col-6 rounded landingBg"
          />
        </div>
      </div>
    </section>
	</div>
	</div>
<%@ include file="/Layout/footer.jsp" %>
