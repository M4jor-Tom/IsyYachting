<template>
  <div>
    <h2 id="page-heading" data-cy="DayHeading">
      <span v-text="$t('isyYachtingApp.day.home.title')" id="day-heading">Days</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('isyYachtingApp.day.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'DayCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-day">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('isyYachtingApp.day.home.createLabel')"> Create a new Day </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && days && days.length === 0">
      <span v-text="$t('isyYachtingApp.day.home.notFound')">No days found</span>
    </div>
    <div class="table-responsive" v-if="days && days.length > 0">
      <table class="table table-striped" aria-describedby="days">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('isyYachtingApp.day.time')">Time</span></th>
            <th scope="row"><span v-text="$t('isyYachtingApp.day.forecastedWeather')">Forecasted Weather</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="day in days" :key="day.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DayView', params: { dayId: day.id } }">{{ day.id }}</router-link>
            </td>
            <td>{{ day.time ? $d(Date.parse(day.time), 'short') : '' }}</td>
            <td v-text="$t('isyYachtingApp.CancelingWeather.' + day.forecastedWeather)">{{ day.forecastedWeather }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'DayView', params: { dayId: day.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'DayEdit', params: { dayId: day.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(day)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="isyYachtingApp.day.delete.question" data-cy="dayDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-day-heading" v-text="$t('isyYachtingApp.day.delete.question', { id: removeId })">
          Are you sure you want to delete this Day?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-day"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeDay()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./day.component.ts"></script>
