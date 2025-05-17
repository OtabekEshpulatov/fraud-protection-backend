const BASE_URL = 'http://localhost:8080';

function getPostIdFromUrl() {
    const pathParts = window.location.pathname.split('/').filter(Boolean);
    return pathParts[pathParts.length - 1];
}

async function fetchPost(postId) {
    const res = await fetch(`${BASE_URL}/api/v1/posts/${postId}`);
    if (!res.ok) throw new Error('Failed to fetch post');
    return res.json();
}

async function fetchSimilarPosts(postId) {
    const res = await fetch(`${BASE_URL}/api/v1/posts/get-similar?postId=${postId}`);
    if (!res.ok) throw new Error('Failed to fetch similar posts');
    return res.json();
}

function renderMediaSlider(mediaUrls) {
    const slider = document.getElementById('media-slider');
    slider.innerHTML = '';
    mediaUrls.forEach(url => {
        const img = document.createElement('img');
        img.src = url;
        img.loading = 'lazy'; // Lazy-load images
        img.alt = 'Media';
        slider.appendChild(img);
    });
}

function renderPostContent(post) {
    const content = document.getElementById('post-content');
    const author = post.user?.username ?? 'Unknown';
    content.innerHTML = `
        <h1>${post.title}</h1>
        <p><strong>Author:</strong> ${author}</p>
        <p>${post.body}</p>
        <p><small>Views: ${post.views ?? 0}, Comments: ${post.comments ?? 0}</small></p>
    `;
}

function renderRelatedPosts(posts) {
    const container = document.getElementById('related-posts');
    container.innerHTML = '';
    posts.forEach(post => {
        const div = document.createElement('div');
        div.className = 'post';
        div.innerHTML = `
            <h4>${post.title}</h4>
            <p>${post.body.slice(0, 100)}...</p>
            <a href="/web-posts/${post.id}">Read more</a>
        `;
        container.appendChild(div);
    });
}

async function init() {
    const postId = getPostIdFromUrl();
    const loading = document.getElementById('loading');
    if (loading) loading.style.display = 'block';

    try {
        const post = await fetchPost(postId);
        renderPostContent(post);
        renderMediaSlider(post.mediaUrls || []);

        const similarPosts = await fetchSimilarPosts(postId);
        renderRelatedPosts(similarPosts);
    } catch (err) {
        console.error('Error loading data:', err);
        document.body.innerHTML = '<p style="color:red;">Failed to load post. Please try again later.</p>';
    } finally {
        if (loading) loading.style.display = 'none';
    }
}

init();
