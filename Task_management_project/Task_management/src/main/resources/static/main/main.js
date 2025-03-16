let data = {};
const deleteIconsTasks = document.querySelectorAll('.deleteIconsTasks');
const completeTask = document.querySelectorAll('.completeTask');

function showPage(pageId) {
    const pages = document.querySelectorAll('.page');
    pages.forEach(page => page.classList.add('hidden'));
    document.getElementById(pageId).classList.remove('hidden');
}

function addTask() {
    const taskName = prompt('Enter task name:');
    const dueDate = prompt('Enter due date:');
    const priority = prompt('Enter priority (High, Medium, Low):');

    if (taskName && dueDate && priority) {
        const taskList = document.querySelector('#today .task-category');

        const newTask = document.createElement('div');
        newTask.className = 'task';
        newTask.innerHTML = `
                    <div class="task-info">
                        <span><strong>Task:</strong> ${taskName}</span>
                        <span><strong>Due:</strong> ${dueDate}</span>
                        <span><strong>Priority:</strong> ${priority}</span>
                    </div>
                    <div class="task-actions">
                        <button>Complete</button>
                    </div>
                `;

        taskList.appendChild(newTask);
    } else {
        // add modal here with the right error message
        alert('Task creation canceled or incomplete details.');
    }
}

function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    sidebar.classList.toggle('visible');
}

// JS code to handle form submission to add a new task

document.getElementById("addTaskForm").addEventListener('click', function (event) {
    event.preventDefault();

    // Récupérer l'ID du projet depuis l'URL (ex: /task/getTaskById/1 => id=1)
    const pathParts = window.location.pathname.split('/');
    const projectId = pathParts[pathParts.length - 1];

    if (!projectId) {
        console.error("ID de projet non trouvé dans l'URL");
        return;
    }

    const data = {
        title: document.getElementById('title').value,
        description: document.getElementById('description').value,
        status: document.getElementById('status').value,
        priority: document.getElementById('priority').value,
        deadline: document.getElementById('deadline').value,
        // Ajoutez ces champs si nécessaires
        project: { id: projectId } // Important pour la liaison JPA
    };

    // fetch(`/projects/${projectId}/tasks`, {
    fetch(`/add/${projectId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })

        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const modal = bootstrap.Modal.getInstance(document.getElementById('exampleModal'));
            modal.hide();
            window.location.reload();
        })
        .catch(error => {
            console.error('Erreur lors de l\'ajout de la tâche:', error);
        });
});
/* status filter */

document.getElementById('statusFilter').addEventListener('change', function () {
    const selectedStatus = this.value;
    const tasks = document.querySelectorAll('.task-details');
    tasks.forEach(task => {
        if (selectedStatus === 'ALL') {
            task.style.display = 'block';
        } else {
            const taskStatus = task.getAttribute('data-status');
            task.style.display = taskStatus === selectedStatus ? 'block' : 'none';
        }
    })
});

/* Delete encoded tasks */

deleteIconsTasks.forEach(task => {
    task.addEventListener('click', (e) => {
        const taskId = e.currentTarget.dataset.taskId;
        console.log('delete projectId ', taskId);
        // /deleteTask/{id}
        fetch(`/deleteTask/${taskId}`, {method: 'DELETE'})
            .then(response => {
                if(!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                // Remove task from UI
                e.target.closest('#task-category').remove();
            })

        .catch(error => {
            console.error('Error adding task:', error);
        })
    })

})

// complete task btn

completeTask.forEach(completeBtn => {
    completeBtn.addEventListener('click',(e) => {
        /* const taskId = e.currentTarget.dataset.taskId; */
        // const taskId = this.getAttribute('data-task-id');
        const taskId = e.currentTarget.dataset.taskId;
        console.log('complete button taskId ', taskId);

        // fetch(`/tasks/${taskId}/complete task/getTaskById/{id}`, {
        fetch(`/tasks/${taskId}/complete`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Erreur réseau: ' + response.status);
                }
                return response.json();
            })
            .then( data => {
                if(data.completed) {

                    // Mettre à jour le bouton
                    document.querySelector('.completeTask')
                        .classList.replace('btn-primary','btn-success');

                    document.querySelector('.completeTask span').textContent = 'Completed task';
                }
                console.log(data);
                console.log(data.projectProgress);
            }).catch(error => console.error('Erreur:', error));
    })
})




