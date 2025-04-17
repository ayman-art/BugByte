<!-- # BugByte -->

# 🗨️ BugByte

BugByte is a social media platform where users can ask questions, share answers, and join communities to talk about topics they care about. It’s designed to make it easy to find helpful content, follow interesting people and groups, and stay updated with real-time notifications.

---

## 🚀 Features

- 🧠 **Post Questions & Answers**  
  Share knowledge by posting questions and contributing answers.

- 💬 **Replies and Discussions**  
  Engage in deeper conversations with replies on questions and answers.

- 👍👎 **Upvote / Downvote System**  
  Users can vote on posts to surface quality content. Each user’s reputation evolves based on their contributions.

- 🎖️ **Reputation System**  
  Users earn or lose reputation points based on votes, fostering a merit-based community.

- 🧑‍🤝‍🧑 **User Roles**

  - **Regular Users**: Create content, interact, join communities.
  - **Admins**: Manage users, monitor activity, moderate content.

- 🏘️ **Groups & Communities**

  - Create and manage interest-based groups.
  - Browse a rich list of communities by category or popularity.

- ➕ **Follow / Unfollow System**  
  Stay connected with users and communities that align with your interests.

- 🔍 **Search & Filtering (Powered by Elasticsearch)**  
  Quickly find questions, answers, or users using powerful text-based and filter-based search capabilities.

---

## 🔎 Search & Filtering with Elasticsearch

The search system leverages **Elasticsearch** to provide a fast, scalable, and intelligent querying experience. Users can:

- Search posts by keywords, tags, or groups.
- Get typo-tolerant and ranked results based on relevance scoring.

This integration ensures highly performant and contextually accurate content discovery even with large datasets.

---

## 🎯 Recommendation System

Our recommendation engine delivers personalized content by considering:

- Posts from followed users.
- Posts in joined communities.
- Posts from other communities to introduce **content diversity**.

To ensure **low-latency responses**, recommendations are **precomputed and cached using Redis**. This provides users with an immediate, relevant feed tailored to their interests and activity patterns.

Key benefits:

- Fast retrieval from Redis.
- Balanced content between relevance and exploration.
- The cache is updated whenever a new relevant post is created, ensuring users always see fresh and personalized content.

---

## 📣 Real-Time Notifications with Kafka

BugByte includes a real-time notification system built on **Apache Kafka**, enabling instant updates across the platform.

- Each user is assigned a **dedicated Kafka channel**.
- Users are notified when:
  - Their post is answered.
  - Someone replies to their comment.
  - They receive votes.
  - Someone they follow creates a new post.

Kafka ensures scalability and high throughput, supporting thousands of concurrent users while maintaining low-latency delivery.

---

## 🛠️ Tech Stack

| Layer         | Technology        |
| ------------- | ----------------- |
| Backend       | Java, Spring Boot |
| Frontend      | React.js          |
| Search Engine | Elasticsearch     |
| Cache Layer   | Redis             |
| Messaging     | Apache Kafka      |
| Database      | MySQL             |

---

## 📦 Installation & Setup

1. **Clone the repository**

   ```bash
   git clone https://github.com/ayman-art/BugByte.git
   cd BugByte
   ```

2. **Backend Setup (Spring Boot)**
   - Navigate to **BugByte-backend/** directory
   - Configure `application.yml` with DB, Kafka, Redis, and Elasticsearch credentials.
   - Run:
   ```bash
   cd BugByte-backend
   ./mvnw spring-boot:run
   ```
3. Frontend Setup (React)
   - Navigate to **bugbyte/** directory:
   ```bash
   cd bugbyte
   npm install
   npm start
   ```
4. Elasticsearch, Redis, Kafka
   Ensure the services are running locally or accessible remotely.

## License

This project is open source and available under the MIT License.
