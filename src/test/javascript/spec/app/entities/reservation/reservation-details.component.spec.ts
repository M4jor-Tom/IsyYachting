/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ReservationDetailComponent from '@/entities/reservation/reservation-details.vue';
import ReservationClass from '@/entities/reservation/reservation-details.component';
import ReservationService from '@/entities/reservation/reservation.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Reservation Management Detail Component', () => {
    let wrapper: Wrapper<ReservationClass>;
    let comp: ReservationClass;
    let reservationServiceStub: SinonStubbedInstance<ReservationService>;

    beforeEach(() => {
      reservationServiceStub = sinon.createStubInstance<ReservationService>(ReservationService);

      wrapper = shallowMount<ReservationClass>(ReservationDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { reservationService: () => reservationServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundReservation = { id: 123 };
        reservationServiceStub.find.resolves(foundReservation);

        // WHEN
        comp.retrieveReservation(123);
        await comp.$nextTick();

        // THEN
        expect(comp.reservation).toBe(foundReservation);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundReservation = { id: 123 };
        reservationServiceStub.find.resolves(foundReservation);

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
