#!/bin/bash


echo "🚀 Content Moderation System - Manual Testing Script"
echo "===================================================="

echo "📡 Checking if application is running on localhost:8080..."
if ! curl -s http://localhost:8080/tags > /dev/null; then
    echo "❌ Application is not running. Please start it with: ./gradlew bootRun"
    exit 1
fi
echo "✅ Application is running!"

echo ""
echo "👤 Step 1: Creating user 'Muthu'..."
USER_RESPONSE=$(curl -s -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "user": {
      "username": "Muthu",
      "email": "muthu@example.com",
      "password": "password123"
    }
  }')

if echo "$USER_RESPONSE" | grep -q "token"; then
    echo "✅ User 'Muthu' created successfully!"
    TOKEN=$(echo "$USER_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
    echo "🔑 JWT Token: $TOKEN"
else
    echo "❌ Failed to create user. Response: $USER_RESPONSE"
    exit 1
fi

echo ""
echo "📰 Step 2: Creating 4 test articles..."

echo "📝 Creating Article 1: Technology Review..."
ARTICLE1_RESPONSE=$(curl -s -X POST http://localhost:8080/articles \
  -H "Content-Type: application/json" \
  -H "Authorization: Token $TOKEN" \
  -d '{
    "article": {
      "title": "Spring Boot Best Practices 2025",
      "description": "A comprehensive guide to Spring Boot development",
      "body": "Spring Boot has revolutionized Java development by providing auto-configuration and embedded servers. This article covers the latest best practices for building robust applications with Spring Boot, including security, testing, and deployment strategies.",
      "tagList": ["spring-boot", "java", "backend", "technology"]
    }
  }')

if echo "$ARTICLE1_RESPONSE" | grep -q "slug"; then
    ARTICLE1_SLUG=$(echo "$ARTICLE1_RESPONSE" | grep -o '"slug":"[^"]*"' | cut -d'"' -f4)
    echo "✅ Article 1 created! Slug: $ARTICLE1_SLUG"
else
    echo "❌ Failed to create Article 1"
fi

echo "📝 Creating Article 2: Tutorial..."
ARTICLE2_RESPONSE=$(curl -s -X POST http://localhost:8080/articles \
  -H "Content-Type: application/json" \
  -H "Authorization: Token $TOKEN" \
  -d '{
    "article": {
      "title": "Building REST APIs with Content Moderation",
      "description": "Step-by-step tutorial for implementing content moderation",
      "body": "Content moderation is essential for any platform that allows user-generated content. This tutorial walks through implementing a robust reporting system that allows users to flag inappropriate content and provides admin tools for managing reports.",
      "tagList": ["tutorial", "api", "moderation", "security"]
    }
  }')

if echo "$ARTICLE2_RESPONSE" | grep -q "slug"; then
    ARTICLE2_SLUG=$(echo "$ARTICLE2_RESPONSE" | grep -o '"slug":"[^"]*"' | cut -d'"' -f4)
    echo "✅ Article 2 created! Slug: $ARTICLE2_SLUG"
else
    echo "❌ Failed to create Article 2"
fi

echo "📝 Creating Article 3: Opinion Piece..."
ARTICLE3_RESPONSE=$(curl -s -X POST http://localhost:8080/articles \
  -H "Content-Type: application/json" \
  -H "Authorization: Token $TOKEN" \
  -d '{
    "article": {
      "title": "The Future of Web Development",
      "description": "My thoughts on where web development is heading",
      "body": "Web development continues to evolve rapidly. From server-side rendering to JAMstack, from monoliths to microservices, developers have more choices than ever. This article explores emerging trends and technologies that will shape the future of web development.",
      "tagList": ["opinion", "web-development", "future", "trends"]
    }
  }')

if echo "$ARTICLE3_RESPONSE" | grep -q "slug"; then
    ARTICLE3_SLUG=$(echo "$ARTICLE3_RESPONSE" | grep -o '"slug":"[^"]*"' | cut -d'"' -f4)
    echo "✅ Article 3 created! Slug: $ARTICLE3_SLUG"
else
    echo "❌ Failed to create Article 3"
fi

echo "📝 Creating Article 4: Technical Deep Dive..."
ARTICLE4_RESPONSE=$(curl -s -X POST http://localhost:8080/articles \
  -H "Content-Type: application/json" \
  -H "Authorization: Token $TOKEN" \
  -d '{
    "article": {
      "title": "GraphQL vs REST: A Comprehensive Comparison",
      "description": "Deep dive into GraphQL and REST API architectures",
      "body": "Both GraphQL and REST have their place in modern API design. This comprehensive comparison examines the strengths and weaknesses of each approach, covering performance, caching, tooling, and developer experience. Learn when to choose GraphQL over REST and vice versa.",
      "tagList": ["graphql", "rest", "api", "architecture", "comparison"]
    }
  }')

if echo "$ARTICLE4_RESPONSE" | grep -q "slug"; then
    ARTICLE4_SLUG=$(echo "$ARTICLE4_RESPONSE" | grep -o '"slug":"[^"]*"' | cut -d'"' -f4)
    echo "✅ Article 4 created! Slug: $ARTICLE4_SLUG"
else
    echo "❌ Failed to create Article 4"
fi

echo ""
echo "📊 SETUP COMPLETE - Summary"
echo "=========================="
echo "👤 User: Muthu (muthu@example.com)"
echo "🔑 Token: $TOKEN"
echo ""
echo "📰 Articles Created:"
echo "1. $ARTICLE1_SLUG - Spring Boot Best Practices 2025"
echo "2. $ARTICLE2_SLUG - Building REST APIs with Content Moderation"
echo "3. $ARTICLE3_SLUG - The Future of Web Development"
echo "4. $ARTICLE4_SLUG - GraphQL vs REST: A Comprehensive Comparison"

echo ""
echo "🚨 Step 3: Testing Content Moderation Functionality..."

echo "📋 Reporting Article 1 for SPAM..."
REPORT1_RESPONSE=$(curl -s -X POST http://localhost:8080/reports/articles/$ARTICLE1_SLUG \
  -H "Content-Type: application/json" \
  -H "Authorization: Token $TOKEN" \
  -d '{
    "report": {
      "reason": "SPAM"
    }
  }')

if echo "$REPORT1_RESPONSE" | grep -q "report"; then
    echo "✅ Article 1 reported for SPAM"
else
    echo "❌ Failed to report Article 1"
fi

echo "📋 Reporting Article 2 for HARASSMENT..."
REPORT2_RESPONSE=$(curl -s -X POST http://localhost:8080/reports/articles/$ARTICLE2_SLUG \
  -H "Content-Type: application/json" \
  -H "Authorization: Token $TOKEN" \
  -d '{
    "report": {
      "reason": "HARASSMENT"
    }
  }')

if echo "$REPORT2_RESPONSE" | grep -q "report"; then
    echo "✅ Article 2 reported for HARASSMENT"
else
    echo "❌ Failed to report Article 2"
fi

echo "📋 Reporting Article 3 for INAPPROPRIATE_CONTENT..."
REPORT3_RESPONSE=$(curl -s -X POST http://localhost:8080/reports/articles/$ARTICLE3_SLUG \
  -H "Content-Type: application/json" \
  -H "Authorization: Token $TOKEN" \
  -d '{
    "report": {
      "reason": "INAPPROPRIATE_CONTENT"
    }
  }')

if echo "$REPORT3_RESPONSE" | grep -q "report"; then
    echo "✅ Article 3 reported for INAPPROPRIATE_CONTENT"
else
    echo "❌ Failed to report Article 3"
fi

echo ""
echo "👨‍💼 Step 4: Admin - Viewing All Reports..."
ADMIN_REPORTS_RESPONSE=$(curl -s -X GET http://localhost:8080/admin/reports \
  -H "Authorization: Token $TOKEN")

if echo "$ADMIN_REPORTS_RESPONSE" | grep -q "reports"; then
    echo "✅ Admin reports retrieved successfully"
    echo "📊 Reports Response: $ADMIN_REPORTS_RESPONSE"
else
    echo "❌ Failed to retrieve admin reports"
fi

echo ""
echo "🧪 MANUAL TESTING INSTRUCTIONS"
echo "=============================="
echo ""
echo "Now you can manually test the following endpoints:"
echo ""
echo "🔍 View all articles:"
echo "curl -H \"Authorization: Token $TOKEN\" http://localhost:8080/articles"
echo ""
echo "📝 Add a comment to Article 1:"
echo "curl -X POST http://localhost:8080/articles/$ARTICLE1_SLUG/comments \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -H \"Authorization: Token $TOKEN\" \\"
echo "  -d '{\"comment\":{\"body\":\"Great article! Very informative.\"}}'"
echo ""
echo "🚨 Report a comment (replace COMMENT_ID):"
echo "curl -X POST http://localhost:8080/reports/articles/$ARTICLE1_SLUG/comments/COMMENT_ID \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -H \"Authorization: Token $TOKEN\" \\"
echo "  -d '{\"report\":{\"reason\":\"SPAM\"}}'"
echo ""
echo "👨‍💼 Admin - Filter reports by status:"
echo "curl -H \"Authorization: Token $TOKEN\" http://localhost:8080/admin/reports?status=PENDING"
echo ""
echo "✏️ Admin - Update report status (replace REPORT_ID):"
echo "curl -X PUT http://localhost:8080/admin/reports/REPORT_ID/status?status=REVIEWED \\"
echo "  -H \"Authorization: Token $TOKEN\""
echo ""
echo "🎉 Content Moderation System is ready for testing!"
echo "User: Muthu | Token: $TOKEN"
