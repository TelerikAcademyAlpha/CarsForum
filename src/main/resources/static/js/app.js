// ================= ELEMENTS =================

const authModal = document.getElementById("authModal");

const loginButton = document.getElementById("loginButton");

const createPostButton = document.getElementById("createPostButton");

const loginTab = document.getElementById("loginTab");

const registerTab = document.getElementById("registerTab");

const loginForm = document.getElementById("loginForm");

const registerForm = document.getElementById("registerForm");

const loginError = document.getElementById("loginError");

const showRegisterInput = document.getElementById("showRegister");

const openRegister = document.getElementById("openRegister");

const openLogin = document.getElementById("openLogin");

const createModal = document.getElementById("createPostModal");

const newPostButton = document.getElementById("newPostButton");

const guestCreatePostButton = document.getElementById("guestCreatePostButton");

const editBtn = document.getElementById("openEditProfile");

const saveBtn = document.getElementById("saveBtn");


// ================= MODAL =================

function openModal() {

    console.log("Modal clicked");

    authModal.style.display = "flex";
}

function closeModal() {
    authModal.style.display = "none";
}


// ================= LOGIN TAB =================

function showLogin() {

    loginForm.style.display = "block";
    registerForm.style.display = "none";

    loginTab.classList.add("active");
    registerTab.classList.remove("active");
}


// ================= REGISTER TAB =================

function showRegisterTab() {

    loginForm.style.display = "none";
    registerForm.style.display = "block";

    registerTab.classList.add("active");
    loginTab.classList.remove("active");
}


// ================= LOGIN ERROR =================

if (loginError && loginError.value === "true") {

    openModal();
    showLogin();

}


// ================= REGISTER ERROR =================

if (showRegisterInput &&
    showRegisterInput.value === "true") {

    openModal();
    showRegisterTab();

}


// ================= BUTTONS =================

if (loginButton) {

    loginButton.addEventListener("click", openModal);

}



// ================= CLICK OUTSIDE MODAL =================

window.addEventListener("click", function (event) {

    if (event.target === authModal) {
        closeModal();
    }

    if (event.target === createModal) {
        closeCreatePostModal();
    }

    if (event.key === "Escape") {
        closeModal();
        closeCreatePostModal();
    }

});


// ================= ESC KEY =================

document.addEventListener("keydown", function (event) {

    if (event.key === "Escape") {

        closeModal();

    }

});


// ================= TABS =================

if (loginTab && registerTab && loginForm && registerForm) {

    loginTab.addEventListener("click", showLogin);

    registerTab.addEventListener("click", showRegisterTab);

    showLogin();
}


// ================= open close modals ==========
if (openRegister) {
    openRegister.addEventListener("click", showRegisterTab);
}

if (openLogin) {
    openLogin.addEventListener("click", showLogin);
}

// ================= posts modal =============
function openCreatePostModal(){

    createModal.style.display = "flex";

}

function closeCreatePostModal() {

    createModal.style.display = "none";

}

if(newPostButton){
    newPostButton.addEventListener(
        "click",
        openCreatePostModal
    );
}

if(createPostButton){
    createPostButton.addEventListener(
        "click",
        openCreatePostModal
    );
}

if (guestCreatePostButton) {
    guestCreatePostButton.addEventListener(
        "click",
        openModal
    );
}

// ========== reply comment ========

document.querySelectorAll(".replyButton").forEach(button => {

    button.addEventListener("click", () => {

        const id = button.dataset.id;

        const form = document.getElementById("reply-" + id);

        if (form.style.display === "block") {

            form.style.display = "none";

        } else {

            form.style.display = "block";

        }

    });

});

// edit profile
if (editBtn && saveBtn) {

    editBtn.addEventListener("click", () => {

        document.querySelectorAll("#profileForm input")
            .forEach(input => {

                if (input.name !== "") {
                    input.disabled = false;
                }

            });

        editBtn.style.display = "none";
        saveBtn.style.display = "inline-block";

    });

}


// ================= MY POSTS =================

document.querySelectorAll(".edit-btn").forEach(button => {

    button.addEventListener("click", () => {

        const id = button.dataset.id;

        const title = document.getElementById("title-" + id);
        const content = document.getElementById("content-" + id);

        const save = document.getElementById("save-" + id);
        const cancel = document.getElementById("cancel-" + id);
        const view = document.getElementById("view-" + id);

        title.disabled = false;
        content.disabled = false;

        button.style.display = "none";
        save.style.display = "inline-block";
        cancel.style.display = "inline-block";
        view.style.display = "none";

    });

});

document.querySelectorAll(".cancel-btn").forEach(button => {

    button.addEventListener("click", () => {

        location.reload();

    });

});


// edit comment
// ================= EDIT COMMENTS =================

document.querySelectorAll(".edit-comment-btn").forEach(button => {

    button.addEventListener("click", () => {

        const id = button.dataset.id;

        const textarea = document.getElementById("comment-" + id);

        const save = document.getElementById("save-comment-" + id);

        const cancel = document.getElementById("cancel-comment-" + id);

        textarea.disabled = false;

        button.style.display = "none";

        save.style.display = "inline-block";

        cancel.style.display = "inline-block";

    });

});

document.querySelectorAll(".cancel-comment-btn").forEach(button => {

    button.addEventListener("click", () => {

        location.reload();

    });

});