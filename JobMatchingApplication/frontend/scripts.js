document.addEventListener("DOMContentLoaded", loadJobs);

function loadJobs() {
    // Call backend API to get job data (mocking API call for this example)
    fetch("/api/jobs") // Update endpoint as needed
        .then(response => response.json())
        .then(data => displayJobs(data));
}

function displayJobs(jobs) {
    const jobList = document.getElementById("job-list");
    jobList.innerHTML = "";
    jobs.forEach(job => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${job.title}</td>
            <td>${job.company}</td>
            <td>${job.location}</td>
            <td>
                <button onclick="editJob(${job.id})">Edit</button>
                <button onclick="deleteJob(${job.id})">Delete</button>
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
        document.getElementById("title").value = job.title;
        document.getElementById("company").value = job.company;
        document.getElementById("location").value = job.location;
    } else {
        document.getElementById("modal-title").innerText = "Add Job";
        document.getElementById("job-form").reset();
    }
}

function closeJobForm() {
    document.getElementById("job-form-modal").style.display = "none";
}

function deleteJob(id) {
    if (confirm("Are you sure you want to delete this job?")) {
        fetch(`/api/jobs/${id}`, { method: "DELETE" })
            .then(() => loadJobs());
    }
}

document.getElementById("job-form").addEventListener("submit", event => {
    event.preventDefault();
    const jobData = {
        title: document.getElementById("title").value,
        company: document.getElementById("company").value,
        location: document.getElementById("location").value,
    };

    const method = jobData.id ? "PUT" : "POST";
    const url = jobData.id ? `/api/jobs/${jobData.id}` : "/api/jobs";

    fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(jobData)
    }).then(() => {
        closeJobForm();
        loadJobs();
    });
});
