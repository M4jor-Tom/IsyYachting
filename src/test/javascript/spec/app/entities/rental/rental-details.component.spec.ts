/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import RentalDetailComponent from '@/entities/rental/rental-details.vue';
import RentalClass from '@/entities/rental/rental-details.component';
import RentalService from '@/entities/rental/rental.service';
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
  describe('Rental Management Detail Component', () => {
    let wrapper: Wrapper<RentalClass>;
    let comp: RentalClass;
    let rentalServiceStub: SinonStubbedInstance<RentalService>;

    beforeEach(() => {
      rentalServiceStub = sinon.createStubInstance<RentalService>(RentalService);

      wrapper = shallowMount<RentalClass>(RentalDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { rentalService: () => rentalServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundRental = { id: 123 };
        rentalServiceStub.find.resolves(foundRental);

        // WHEN
        comp.retrieveRental(123);
        await comp.$nextTick();

        // THEN
        expect(comp.rental).toBe(foundRental);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundRental = { id: 123 };
        rentalServiceStub.find.resolves(foundRental);

        // WHEN
        comp.beforeRouteEnter({ params: { rentalId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.rental).toBe(foundRental);
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
