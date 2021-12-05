<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="day">
        <h2 class="jh-entity-heading" data-cy="dayDetailsHeading">
          <span v-text="$t('isyYachtingApp.day.detail.title')">Day</span> {{ day.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('isyYachtingApp.day.time')">Time</span>
          </dt>
          <dd>
            <span v-if="day.time">{{ $d(Date.parse(day.time), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('isyYachtingApp.day.forecastedWeather')">Forecasted Weather</span>
          </dt>
          <dd>
            <span v-text="$t('isyYachtingApp.CancelingWeather.' + day.forecastedWeather)">{{ day.forecastedWeather }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="day.id" :to="{ name: 'DayEdit', params: { dayId: day.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./day-details.component.ts"></script>
