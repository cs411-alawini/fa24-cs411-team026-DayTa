document.addEventListener("DOMContentLoaded", loadJobs);

function loadJobs() {
    // Call backend API to get job data
    fetch("/api/jobs")
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => displayJobs(data))
        .catch(error => console.error('Error fetching jobs:', error));
}

function performSearch(query) {
    fetch(`/api/jobs/search?keyword=${encodeURIComponent(query)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(jobs => {
            populateJobTable(jobs);
        })
        .catch(error => {
            console.error('Error searching jobs:', error);
            alert(`Error: ${error.message}`);
        });
}

function populateJobTable(jobs) {
    const tableBody = document.querySelector("#jobs-table tbody");
    tableBody.innerHTML = ""; // Clear the current rows

    jobs.forEach(job => {
        const row = `
            <tr>
                <td>${job.jobId}</td>
                <td>${job.title}</td>
                <td>${job.category}</td>
                <td>${job.location}</td>
                <td>${job.type}</td>
                <td>${job.skillsKeyWord}</td>
                <td>
                    <button onclick="editJob(${job.jobId})">Edit</button>
                    <button onclick="deleteJob(${job.jobId})">Delete</button>
                </td>
            </tr>
        `;
        tableBody.innerHTML += row;
    });
}

function displayJobs(jobs) {
    const jobList = document.getElementById("job-list");
    jobList.innerHTML = "";
    jobs.forEach(job => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${sanitizeHTML(job.title)}</td>
            <td>${sanitizeHTML(job.companyId)}</td>
            <td>${sanitizeHTML(job.category)}</td>
            <td>${sanitizeHTML(job.location)}</td>
            <td>${sanitizeHTML(job.duration)}</td>
            <td>${sanitizeHTML(job.type)}</td>
            <td>${sanitizeHTML(job.skillsKeyWord)}</td>
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
        const cells = row.getElementsByTagName("td");
        let match = false;
        // Iterate through all cells except the last one (Actions)
        for (let i = 0; i < cells.length - 1; i++) {
            const cellText = cells[i].innerText.toLowerCase();
            if (cellText.includes(query)) {
                match = true;
                break;
            }
        }
        row.style.display = match ? "" : "none";
    });
}

function openJobForm(job = null) {
    console.debug("Job data for form:", job);
    const modal = document.getElementById("job-form-modal");
    modal.style.display = "flex";
    
    if (job) {
        document.getElementById("modal-title").innerText = "Edit Job";
        document.getElementById("title").value = job.title;
        document.getElementById("companyId").value = job.companyId;
        document.getElementById("category").value = job.category;
        document.getElementById("location").value = job.location;
        document.getElementById("duration").value = job.duration;
        document.getElementById("type").value = job.type;
        document.getElementById("skillsKeyWord").value = job.skillsKeyWord;
        document.getElementById("job-form").dataset.jobId = job.jobId; // Store jobId for editing
    } else {
        document.getElementById("modal-title").innerText = "Add New Job";
        document.getElementById("job-form").reset();
        delete document.getElementById("job-form").dataset.jobId;
    }
}

function closeJobForm() {
    const modal = document.getElementById("job-form-modal");
    modal.style.display = "none";
}

// Function to delete a job
function deleteJob(id) {
    if (confirm("Are you sure you want to delete this job?")) {
        fetch(`/api/jobs/${id}`, { method: "DELETE" })
            .then(response => {
                if (response.status === 204) {
                    alert("Job deleted successfully!");
                    loadJobs();
                } else {
                    return response.text().then(text => { throw new Error(text) });
                }
            })
            .catch(error => {
                console.error('Error deleting job:', error);
                alert(`Error: ${error.message}`);
            });
    }
}

// Function to edit a job
function editJob(id) {
    fetch(`/api/jobs/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(job => {
            openJobForm(job);
        })
        .catch(error => {
            console.error('Error fetching job details:', error);
            alert(`Error: ${error.message}`);
        });
}

// Function to sanitize HTML to prevent XSS attacks
function sanitizeHTML(str) {
    var temp = document.createElement('div');
    temp.textContent = str;
    return temp.innerHTML;
}

// Handle form submission for adding/editing jobs
document.getElementById("job-form").addEventListener("submit", function(event) {
    event.preventDefault(); // Prevent the default form submission

    const jobId = event.target.dataset.jobId; // Get jobId if editing
    const jobData = {
        companyId: parseInt(document.getElementById("companyId").value),
        title: document.getElementById("title").value.trim(),
        category: document.getElementById("category").value.trim(),
        location: document.getElementById("location").value.trim(),
        duration: parseInt(document.getElementById("duration").value),
        type: document.getElementById("type").value.trim(),
        skillsKeyWord: document.getElementById("skillsKeyWord").value.trim()
    };

    // Validate form data (basic validation)
    if (isNaN(jobData.companyId) || isNaN(jobData.duration)) {
        alert("Company ID and Duration must be valid numbers.");
        return;
    }

    let method = "POST";
    let url = "/api/jobs";
    if (jobId) {
        method = "PUT";
        url = `/api/jobs/${jobId}`;
    }

    fetch(url, {
        method: method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(jobData)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            return response.text().then(text => { throw new Error(text) });
        }
    })
    .then(data => {
        if (method === "POST") {
            alert("Job added successfully!");
        } else {
            alert("Job updated successfully!");
        }
        closeJobForm();
        loadJobs(); // Refresh the job list
    })
    .catch(error => {
        console.error('Error saving job:', error);
        alert(`Error: ${error.message}`);
    });
});