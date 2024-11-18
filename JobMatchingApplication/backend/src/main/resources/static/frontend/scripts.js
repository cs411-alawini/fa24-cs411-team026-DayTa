// scripts.js

document.addEventListener("DOMContentLoaded", loadJobs);

function loadJobs() {
    // Call backend API to get job data
    fetch("/api/jobs") // Ensure this matches the updated API path
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => displayJobs(data))
        .catch(error => console.error('Error fetching jobs:', error));
}

function displayJobs(jobs) {
    const jobList = document.getElementById("job-list");
    jobList.innerHTML = "";
    jobs.forEach(job => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${job.title}</td>
            <td>${job.companyId}</td>
            <td>${job.category}</td>
            <td>${job.location}</td>
            <td>${job.duration}</td>
            <td>${job.type}</td>
            <td>${job.skillsKeyWord}</td>
            <td>
                <button onclick="editJob(${job.jobId})">Edit</button>
                <button onclick="deleteJob(${job.jobId})">Delete</button>
            </td>
        `;
        jobList.appendChild(row);
    });
}

function filterJobs() {
    const query = document.getElementById("search-bar").value.toLowerCase();
    const rows = document.querySelectorAll("#jobs-table tbody tr");
    rows.forEach(row => {
        const title = row.cells[0].innerText.toLowerCase();
        row.style.display = title.includes(query) ? "" : "none";
    });
}

function openJobForm(job = null) {
    document.getElementById("job-form-modal").style.display = "flex";
    if (job) {
        document.getElementById("modal-title").innerText = "Edit Job";
        document.getElementById("companyId").value = job.companyId;
        document.getElementById("title").value = job.title;
        document.getElementById("category").value = job.category;
        document.getElementById("location").value = job.location;
        document.getElementById("duration").value = job.duration;
        document.getElementById("type").value = job.type;
        document.getElementById("skillsKeyWord").value = job.skillsKeyWord;
        document.getElementById("job-form").dataset.jobId = job.jobId; // Store jobId for editing
    } else {
        document.getElementById("modal-title").innerText = "Add Job";
        document.getElementById("job-form").reset();
        delete document.getElementById("job-form").dataset.jobId;
    }
}

function closeJobForm() {
    document.getElementById("job-form-modal").style.display = "none";
}

function deleteJob(id) {
    if (confirm("Are you sure you want to delete this job?")) {
        fetch(`/api/jobs/${id}`, { method: "DELETE" })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                loadJobs();
            })
            .catch(error => console.error('Error deleting job:', error));
    }
}

document.getElementById("job-form").addEventListener("submit", event => {
    event.preventDefault();
    
    const jobId = event.target.dataset.jobId; // Get jobId if editing
    const jobData = {
        companyId: parseInt(document.getElementById("companyId").value),
        title: document.getElementById("title").value,
        category: document.getElementById("category").value,
        location: document.getElementById("location").value,
        duration: parseInt(document.getElementById("duration").value),
        type: document.getElementById("type").value,
        skillsKeyWord: document.getElementById("skillsKeyWord").value
    };

    let method = "POST";
    let url = "/api/jobs";
    if (jobId) {
        method = "PUT";
        url = `/api/jobs/${jobId}`;
    }

    fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(jobData)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw new Error(err.message || 'Unknown error'); });
        }
        closeJobForm();
        loadJobs();
    })
    .catch(error => {
        console.error('Error saving job:', error);
        alert(`Error: ${error.message}`);
    });
});