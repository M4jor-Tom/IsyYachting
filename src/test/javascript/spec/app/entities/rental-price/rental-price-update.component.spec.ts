/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import RentalPriceUpdateComponent from '@/entities/rental-price/rental-price-update.vue';
import RentalPriceClass from '@/entities/rental-price/rental-price-update.component';
import RentalPriceService from '@/entities/rental-price/rental-price.service';

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
  describe('RentalPrice Management Update Component', () => {
    let wrapper: Wrapper<RentalPriceClass>;
    let comp: RentalPriceClass;
    let rentalPriceServiceStub: SinonStubbedInstance<RentalPriceService>;

    beforeEach(() => {
      rentalPriceServiceStub = sinon.createStubInstance<RentalPriceService>(RentalPriceService);

      wrapper = shallowMount<RentalPriceClass>(RentalPriceUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          rentalPriceService: () => rentalPriceServiceStub,
          alertService: () => new AlertService(),

          rentalService: () => new RentalService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.rentalPrice = entity;
        rentalPriceServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(rentalPriceServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.rentalPrice = entity;
        rentalPriceServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(rentalPriceServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundRentalPrice = { id: 123 };
        rentalPriceServiceStub.find.resolves(foundRentalPrice);
        rentalPriceServiceStub.retrieve.resolves([foundRentalPrice]);

        // WHEN
        comp.beforeRouteEnter({ params: { rentalPriceId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.rentalPrice).toBe(foundRentalPrice);
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
