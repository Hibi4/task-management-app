// JS code to handle form submission to add a new project

let data = {}
const deleteIcons = document.querySelectorAll('.delete-icon');
const deadlineButtons = document.querySelectorAll('.update-deadline');

document.getElementById("addProjectForm").addEventListener('click', function (event) {
    console.log("Project Form submitted");
    event.preventDefault();

    data = {
        title : document.getElementById('title').value,
        description: document.getElementById('textareaDescription').value,
        deadline: document.getElementById('deadline').value

    };

    console.log(data);
    fetch('/addProject', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    }).then(response => {
        if(!response.ok) {
           throw new Error(`HTTP error! status: ${response.status}`);
        }
        const modal = bootstrap.Modal.getInstance(document.getElementById('projectModal'));
        modal.hide();
        window.location.reload();
    }).catch(error => {
        console.error('Error adding project:', error);
    })

});

/* document.getElementById("delete-icon").addEventListener('click', function (event) {
    console.log("project deleted")
}) */

deleteIcons.forEach(icon => {
    icon.addEventListener('click', function(event) {
        const projectId = this.dataset.projectId;
        console.log('delete projectId ', projectId);
        fetch(`/deleteProject/${projectId}`, { method: 'DELETE' })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                // Remove the deleted project from the UI
                event.target.closest('.category-project').remove();
            })
            .catch(error => {
                console.error('Error deleting project:', error);
            });
    });
});

// update progress bar task
/* function updateProgressBar(projectId, progress) {
    const progressBar = document.querySelector(`[data-project-id="${projectId}"] .progress-bar`);
    if (progressBar) {
        progressBar.style.width = `${progress}%`;
        progressBar.textContent = `${progress}%`;
        progressBar.setAttribute('aria-valuenow', progress);
    }
} */

function updateProgressBar(projectId, progress) {
    const progressBar = document.querySelector(`[data-project-id="${projectId}"] .progress-bar`);
    if(progressBar) {
        progressBar.style.width = `${progress}%`;
        progressBar.textContent = `${progress}%`;
        progressBar.setAttribute('aria-valuenow', progress);
    }
}

function handleTaskCompletion(ProjectId) {
    fetch(`/tasks/${ProjectId}`, {}).then(response => {
        response.json().then(data => {
            updateProgressBar(ProjectId, data.progress);
        })
    })
}

// update project's deadline
deadlineButtons.forEach(deadlineButton => {
    deadlineButton.addEventListener('click', function(e) {
        const projectId = e.currentTarget.dataset.projectId;
        // const newDeadline = document.getElementById('deadline-' + projectId).value;
        console.log("Nieuwe deadline: ", projectId);
    })
})
