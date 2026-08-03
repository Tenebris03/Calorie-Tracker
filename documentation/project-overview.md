# Project Overview

## Project Name

Helix is a personal Android calorie and health tracking application. It allows a user to record food, track weight, calculate calorie targets, recognize food, and receive context-aware coaching.

## Problem Statement

Many calorie tracking applications focus on recording data but do not provide context-aware feedback. Helix combines ordinary tracking features with an optional AI coach that considers a user's calorie goal, recent behavior, time of day, calendar, and weather.

The project is designed as a single-user, single-device demonstration rather than a production service with accounts and synchronization.

## Main Objectives

- Let a user create a nutrition profile during onboarding.
- Calculate a daily calorie and protein target.
- Record food manually or through barcode and photo flows.
- Display calories and macronutrients for a selected date.
- Track weight over time and compare it with a target weight.
- Use a local model to filter possible coaching events.
- Use cloud Gemini reasoning for the final coaching decision.
- Deliver a notification when Gemini produces a critical alert.

## Target User

The target user is an individual who wants a lightweight personal food and weight tracker. The application assumes one person uses one phone. There are no accounts, cloud synchronization, or multi-user features.

## Scope

The application covers:

- Onboarding and personal profile data
- Food logging and editing
- Meal grouping
- Barcode lookup through Open Food Facts
- Photo-based food estimation through Gemini
- Weight history and progress visualization
- Calorie and macro summaries
- Optional calendar and location context
- AI coaching notifications
- Developer test-data seeding

The application does not cover:

- Medical diagnosis
- Clinically validated nutrition advice
- User accounts
- Cross-device synchronization
- Reliable automatic portion measurement
- A complete food database owned by the application

## Typical User Journey

1. The user opens the app for the first time.
2. Onboarding collects gender, age, height, weight, activity level, goal, and calorie offset.
3. The app stores a profile and initial weight entry.
4. The dashboard calculates the daily calorie target and shows the selected day's food log.
5. The user adds food manually, from recent entries, with a barcode, or with a photo.
6. The app updates calorie and macro totals.
7. The local DistilBERT gatekeeper evaluates whether the entry deserves further coaching.
8. If the local probability is high enough, WorkManager schedules the cloud Gemini coach.
9. Gemini analyzes the full context and may create a notification.

## Success Criteria For The Demo

The project demonstrates a complete mobile AI pipeline if it can:

- Run the Android app and complete onboarding.
- Add and display a food entry.
- Use a barcode to retrieve Open Food Facts data.
- Use a photo to request a Gemini nutrition estimate.
- Run the TFLite gatekeeper locally.
- Send a gated event to Gemini.
- Display a coach card or notification when Gemini returns a critical alert.
