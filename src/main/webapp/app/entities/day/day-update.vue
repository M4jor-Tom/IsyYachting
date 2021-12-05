<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="isyYachtingApp.day.home.createOrEditLabel"
          data-cy="DayCreateUpdateHeading"
          v-text="$t('isyYachtingApp.day.home.createOrEditLabel')"
        >
          Create or edit a Day
        </h2>
        <div>
          <div class="form-group" v-if="day.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="day.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.day.time')" for="day-time">Time</label>
            <div class="d-flex">
              <input
                id="day-time"
                data-cy="time"
                type="datetime-local"
                class="form-control"
                name="time"
                :class="{ valid: !$v.day.time.$invalid, invalid: $v.day.time.$invalid }"
                required
                :value="convertDateTimeFromServer($v.day.time.$model)"
                @change="updateInstantField('time', $event)"
              />
            </div>
            <div v-if="$v.day.time.$anyDirty && $v.day.time.$invalid">
              <small class="form-text text-danger" v-if="!$v.day.time.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.day.time.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.day.forecastedWeather')" for="day-forecastedWeather"
              >Forecasted Weather</label
            >
            <select
              class="form-control"
              name="forecastedWeather"
              :class="{ valid: !$v.day.forecastedWeather.$invalid, invalid: $v.day.forecastedWeather.$invalid }"
              v-model="$v.day.forecastedWeather.$model"
              id="day-forecastedWeather"
              data-cy="forecastedWeather"
            >
              <option
                v-for="cancelingWeather in cancelingWeatherValues"
                :key="cancelingWeather"
                v-bind:value="cancelingWeather"
                v-bind:label="$t('isyYachtingApp.CancelingWeather.' + cancelingWeather)"
              >
                {{ cancelingWeather }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.day.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./day-update.component.ts"></script>
