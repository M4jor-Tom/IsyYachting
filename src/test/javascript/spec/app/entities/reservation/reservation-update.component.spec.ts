/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import ReservationUpdateComponent from '@/entities/reservation/reservation-update.vue';
import ReservationClass from '@/entities/reservation/reservation-update.component';
import ReservationService from '@/entities/reservation/reservation.service';

import DayService from '@/entities/day/day.service';

import ClientService from '@/entities/client/client.service';

import RentalService from '@/entities/rental/rental.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Reservation Management Update Component', () => {
    let wrapper: Wrapper<ReservationClass>;
    let comp: ReservationClass;
    let reservationServiceStub: SinonStubbedInstance<ReservationService>;

    beforeEach(() => {
      reservationServiceStub = sinon.createStubInstance<ReservationService>(ReservationService);

      wrapper = shallowMount<ReservationClass>(ReservationUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          reservationService: () => reservationServiceStub,
          alertService: () => new AlertService(),

          dayService: () => new DayService(),

          clientService: () => new ClientService(),

          rentalService: () => new RentalService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.reservation = entity;
        reservationServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(reservationServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.reservation = entity;
        reservationServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(reservationServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundReservation = { id: 123 };
        reservationServiceStub.find.resolves(foundReservation);
        reservationServiceStub.retrieve.resolves([foundReservation]);

        // WHEN
        comp.beforeRouteEnter({ params: { reservationId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.reservation).toBe(foundReservation);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
