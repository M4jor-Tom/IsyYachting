<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="rentalPrice">
        <h2 class="jh-entity-heading" data-cy="rentalPriceDetailsHeading">
          <span v-text="$t('isyYachtingApp.rentalPrice.detail.title')">RentalPrice</span> {{ rentalPrice.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('isyYachtingApp.rentalPrice.value')">Value</span>
          </dt>
          <dd>
            <span>{{ rentalPrice.value }}</span>
          </dd>
          <dt>
            <span v-text="$t('isyYachtingApp.rentalPrice.season')">Season</span>
          </dt>
          <dd>
            <span v-text="$t('isyYachtingApp.Season.' + rentalPrice.season)">{{ rentalPrice.season }}</span>
          </dd>
          <dt>
            <span v-text="$t('isyYachtingApp.rentalPrice.rental')">Rental</span>
          </dt>
          <dd>
            <div v-if="rentalPrice.rental">
              <router-link :to="{ name: 'RentalView', params: { rentalId: rentalPrice.rental.id } }">{{
                rentalPrice.rental.beginingInstant
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="rentalPrice.id"
          :to="{ name: 'RentalPriceEdit', params: { rentalPriceId: rentalPrice.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./rental-price-details.component.ts"></script>
